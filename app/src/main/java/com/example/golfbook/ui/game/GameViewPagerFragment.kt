package com.example.golfbook.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.golfbook.databinding.FragmentViewPagerBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    private val args: GameViewPagerFragmentArgs by navArgs()

    private val viewModel: GameViewModel by viewModels()

    private val mainViewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentViewPagerBinding.inflate(inflater)

        viewModel.intialGame.observe(viewLifecycleOwner) { gameResource ->

            when (gameResource) {

                is Resource.Success -> {

                    binding.progressCircular.visibility = View.GONE

                    val players = gameResource.data.players

                    if (players != null) {

                        mainViewModel.game = gameResource.data

                        val pagerAdapter = IndividualGameViewPagerAdapter(
                                players,
                                args.gameId,
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

        viewModel.initGame(args.gameId)

        if (args.loungeId != null) { // si le joueur est le adminPlayer, il vide le salon

            CoroutineScope(Dispatchers.IO).launch {
                delay(10000)
                viewModel.freeLounge(args.loungeId!!)
            }

        }

        return binding.root
    }

}