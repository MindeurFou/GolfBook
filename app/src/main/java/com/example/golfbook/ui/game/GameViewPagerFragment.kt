package com.example.golfbook.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.golfbook.databinding.FragmentViewPagerBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.utils.Resource

class GameViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    private val args: GameViewPagerFragmentArgs by navArgs()

    private lateinit var viewModelFactory: GameViewModelFactory

    private val viewModel: GameViewModel by viewModels(
            factoryProducer = { viewModelFactory }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentViewPagerBinding.inflate(inflater)

        viewModelFactory = GameViewModelFactory(args)

        viewModel.intialGame.observe(viewLifecycleOwner) { gameResource ->

            when (gameResource) {

                is Resource.Success -> {

                    binding.progressCircular.visibility = View.GONE

                    val players = gameResource.data.players

                    if (players != null) {

                        viewModel.setGame(gameResource)

                        val pagerAdapter = IndividualGameViewPagerAdapter(
                                players,
                                requireActivity().supportFragmentManager,
                                lifecycle
                        )

                        binding.viewPager.adapter = pagerAdapter

                    }
                }

                is Resource.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    gameResource.exception.toast(requireContext())
                }

                is Resource.Loading -> binding.progressCircular.visibility = View.VISIBLE
            }

        }

        return binding.root
    }

}