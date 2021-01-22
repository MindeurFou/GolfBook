package com.example.golfbook.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
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
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.ui.chooseAvatar.ChooseAvatarViewModel
import com.example.golfbook.utils.Resource

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val args: HomeFragmentArgs by navArgs()

    private lateinit var viewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private val mainViewModel: ActivityViewModel by activityViewModels()

    private lateinit var adapter: LoungeAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(inflater)
        viewModelFactory = HomeViewModelFactory()

        // si on vient du fragment chooseAvatar => récupérer le player qui a été choisit
        if (args.playerId != null) {
            viewModel.retrievePlayerAndManagedPlayers(args.playerId!!) // MANAGED PLAYERS DÉSACTIVÉ TANT QUE L'APPLI N'EST PAS PLUS AVANCÉE
        } else {
            bindMainPlayer(mainViewModel.currentPlayer!!)
            viewModel.mainPlayer = mainViewModel.currentPlayer!!
        }






        // =========== Recycler stuff ===========================

        adapter = LoungeAdapter { lounge -> viewModel.setHomeEvent(HomeEvent.JoinLoungeEvent(lounge)) }

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
          mainViewModel.currentPlayer?.let {
                navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.UPDATE_MAIN_PLAYER)
            }
        }

        binding.btnManageOtherPlayer.setOnClickListener {
            mainViewModel.currentPlayer?.let {
                navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.CREATE_MANAGED_PLAYER)
            }
        }

        binding.btnAddCourse.setOnClickListener { navigateToCreateCourseFragment() }

        binding.btnSeeCourses.setOnClickListener { navigateToViewCoursesFragment() }


        return binding.root
    }


    private fun subscribeObservers() {

        viewModel.player.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Loading -> {} // TODO display une image d'attente

                is Resource.Failure -> {
                    resource.exception.toast(requireContext())
                }

                is Resource.Success -> {

                    val player = resource.data

                    bindMainPlayer(player)

                    mainViewModel.currentPlayer = player
                    updateSharedPref(player.playerId!!)

                }
            }
        }



        viewModel.lounges.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Success -> adapter.updateLounges(resource.data)

                is Resource.Failure -> resource.exception.toast(requireContext())

                else -> {}
            }
        }


        viewModel.managedPlayers.observe(viewLifecycleOwner) { listPlayers ->

            binding.managedPlayerLayout.removeAllViews()

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            listPlayers.forEach {
                val textView = TextView(requireContext()).apply {
                    text = it.name
                    layoutParams = params
                    setTextAppearance(R.style.TextAppearance_GolfBook_Guidelines)
                    typeface = resources.getFont(R.font.days_one)
                    maxLines = 1
                }

                binding.managedPlayerLayout.addView(textView)
            }

            binding.managedPlayerLayout.invalidate()
            binding.managedPlayerLayout.requestLayout()
        }

        viewModel.joinLoungeState.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> {}

                is Resource.Failure -> resource.exception.toast(requireContext())

                is Resource.Success -> {
                    navigateToLoungeDetailsFragment(resource.data) // courseId
                }
            }
        }

    }

    private fun navigateToChooseAvatarFragment(chooseAvatarAction: ChooseAvatarViewModel.Companion.chooseAvatarAction) {
        val action = HomeFragmentDirections.actionHomeFragmentToChooseAvatarFragment(chooseAvatarAction = chooseAvatarAction)
        findNavController().navigate(action)
    }

    private fun navigateToCreateCourseFragment() = findNavController().navigate(R.id.action_homeFragment_to_createCourseFragment)

    private fun navigateToViewCoursesFragment() = findNavController().navigate(R.id.action_homeFragment_to_viewCoursesFragment)

    private fun navigateToLoungeDetailsFragment(loungeId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToLoungeDetailsFragment(loungeId)
        findNavController().navigate(action)
    }


    private fun updateSharedPref(playerId: String) {

        val sharedPrefEditor = activity?.getPreferences(Context.MODE_PRIVATE)?.edit()

        sharedPrefEditor?.putString(requireContext().getString(R.string.player_id_key), playerId)?.apply()
    }

    private fun bindMainPlayer(player: Player) {

        val drawableResourceId = if (player.drawableResourceId == -1)
            R.drawable.man1
        else
            player.drawableResourceId

        binding.imageAvatarHome.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableResourceId))

        binding.avatarName.text = player.name

    }

}