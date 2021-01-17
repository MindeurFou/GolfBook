package com.example.golfbook.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golfbook.R
import com.example.golfbook.data.model.Player
import com.example.golfbook.databinding.FragmentHomeBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.extensions.ViewExtensions.findDrawableResourceId
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.ui.chooseAvatar.ChooseAvatarViewModel
import com.example.golfbook.utils.Resource
import java.lang.Exception

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val args: HomeFragmentArgs by navArgs()

    private lateinit var viewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private val mainViewModel: ActivityViewModel by activityViewModels()

    private lateinit var adapter: LoungeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        viewModelFactory = HomeViewModelFactory()
        viewModel.processArgs(args)

        // =========== Recycler stuff ===========================

        adapter = LoungeAdapter(
                { lounge -> viewModel.setHomeEvent(HomeEvent.JoinLoungeEvent(lounge)) },
                { lounge -> viewModel.setHomeEvent(HomeEvent.LeaveLoungeEvent(lounge)) }
        )

        binding.loungeContainer.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(requireContext())

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        else
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.loungeContainer.layoutManager = linearLayoutManager

        binding.loungeContainer.addItemDecoration(LoungeSpacingDecoration())

        // =========== Recycler stuff ===========================


        subscribeObservers()


        binding.btnUpdatePlayer.setOnClickListener {

            val playerResource = viewModel.player.value
            var player: Player? = null

            playerResource?.let {
                if (it is Resource.Success) {
                    player = it.data
                }
            }

            player?.let {
                navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.UPDATE_MAIN_PLAYER)
            }

        }

        binding.btnManageOtherPlayer.setOnClickListener {

            val playerResource = viewModel.player.value
            var player: Player? = null

            playerResource?.let {
                if (it is Resource.Success) {
                    player = it.data
                }
            }

            player?.let {
                navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.CREATE_MANAGED_PLAYER)
            }

        }

        binding.btnAddCourse.setOnClickListener {

        }


        return binding.root
    }


    private fun subscribeObservers() {

        viewModel.player.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Loading -> {}// binding.imageAvatarHome.  TODO display une image d'attente

                is Resource.Failure -> {
                    resource.exception.toast(requireContext())
                    Log.d("mdebug", resource.exception.message.toString())
                }

                is Resource.Success -> {

                    val player = resource.data

                    val drawable = ContextCompat.getDrawable(requireContext(), player.drawableResourceId)
                    binding.imageAvatarHome.setImageDrawable(drawable)

                    binding.avatarName.text = player.name

                    mainViewModel.currentPlayer = player
                    updateSharedPref(player.playerId!!)

                }
            }
        }



        viewModel.lounges.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Loading -> {
                    TODO("ajouter un progressbar et la cacher dans les autres etats")
                }
                is Resource.Success -> {
                    adapter.updateLounges(resource.data)
                }
                is Resource.Failure -> resource.exception.toast(requireContext())
            }
        }


        viewModel.managedPlayers.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Loading -> {}// binding.imageAvatarHome.  TODO display une image d'attente

                is Resource.Failure -> resource.exception.toast(requireContext())

                is Resource.Success -> {} // TODO
            }

        }
    }

    private fun navigateToChooseAvatarFragment(chooseAvatarAction: ChooseAvatarViewModel.Companion.chooseAvatarAction) {

        val action = HomeFragmentDirections.actionHomeFragmentToChooseAvatarFragment(
                chooseAvatarAction = chooseAvatarAction
        )

        findNavController().navigate(action)
    }

    private fun updateSharedPref(playerId: String) {

        val sharedPrefEditor = activity?.getPreferences(Context.MODE_PRIVATE)?.edit()

        sharedPrefEditor?.putString(requireContext().getString(R.string.player_id_key), playerId)?.apply()
    }

}