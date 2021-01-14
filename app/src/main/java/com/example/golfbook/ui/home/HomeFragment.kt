package com.example.golfbook.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golfbook.data.model.Player
import com.example.golfbook.databinding.FragmentHomeBinding
import com.example.golfbook.utils.Resource

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val args: HomeFragmentArgs by navArgs()

    private lateinit var viewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var adapter: LoungeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        viewModelFactory = HomeViewModelFactory(args)

        // =========== Recycler stuff ===========================

        adapter = LoungeAdapter(viewModel)
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

        viewModel.setHomeEvent(HomeEvent.SavePlayerEvent)


        binding.btnUpdatePlayer.setOnClickListener {
            navigateToChooseAvatarFragment(viewModel.viewState.value!!.player, true)
        }

        binding.btnManageOtherPlayer.setOnClickListener {
            navigateToChooseAvatarFragment(viewModel.viewState.value!!.player, false)
        }


        return binding.root
    }


    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->



            val drawable = ContextCompat.getDrawable(requireContext(), viewState.player.avatarResourceId)
            binding.imageAvatarHome.setImageDrawable(drawable)

            binding.avatarName.text = viewState.player.name

            adapter.updateLounges(viewState.lounges)

        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->

            when (dataState) {

                is Resource.Loading -> {
                    TODO("ajouter un progressbar et la cacher dans les autres etats")
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(), dataState.data, Toast.LENGTH_LONG).show()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), dataState.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToChooseAvatarFragment(player: Player, isMainPlayer: Boolean) {
        val action = HomeFragmentDirections.actionHomeFragmentToChooseAvatarFragment(isMainPlayer, player)
        findNavController().navigate(action)
    }

}