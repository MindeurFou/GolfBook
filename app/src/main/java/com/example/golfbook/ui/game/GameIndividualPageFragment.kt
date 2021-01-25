package com.example.golfbook.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golfbook.data.model.Game
import com.example.golfbook.databinding.FragmentGameIndividualBinding
import com.example.golfbook.data.model.Player
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.extensions.ModelExtensions.getSortedLeaderboard
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.ui.adapters.IndividualScoreboardAdapter
import com.example.golfbook.utils.Resource


class GameIndividualPageFragment(
        val player: Player,
        private val gameId: String
) : Fragment() {

    private lateinit var binding: FragmentGameIndividualBinding

    private val viewModel: GameViewModel by viewModels()

    private val mainViewModel: ActivityViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentGameIndividualBinding.inflate(inflater)

        // ======== Avatar ===========
        binding.name.text = player.name
        binding.imageAvatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), player.drawableResourceId))



        // ======== Score ===========
        binding.scoreboard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        binding.scoreboard.adapter = IndividualScoreboardAdapter {
            //viewModel.putScore(player.playerId, it) TODO
        }

        viewModel.setGame(Resource.Success(mainViewModel.game!!))

        viewModel.game.observe(viewLifecycleOwner) { updateScores(it) }


        viewModel.subscribeToScorebook(gameId)



        return binding.root
    }

    private fun updateScores(resource: Resource<Game>) {

        when (resource) {

            is Resource.Loading -> binding.progressCircular.visibility = View.VISIBLE

            is Resource.Failure -> {
                binding.progressCircular.visibility = View.GONE
                resource.exception.toast(requireContext())
            }

            is Resource.Success -> {
                binding.progressCircular.visibility = View.GONE

                val game = resource.data

                val leaderboardData = game.getSortedLeaderboard()

                // bind leaderboard
                leaderboardData.forEachIndexed { index, playerData ->

                    when(index) {

                        0 -> {
                            binding.leaderBoardName1.text = playerData.first
                            binding.leaderBoardScore1.text = playerData.second.toString()
                        }

                        1 -> {
                            binding.leaderBoardName2.text = playerData.first
                            binding.leaderBoardScore2.text = playerData.second.toString()
                        }

                        2 -> {
                            binding.leaderBoardName3.text = playerData.first
                            binding.leaderBoardScore3.text = playerData.second.toString()
                        }

                        3 -> {
                            binding.leaderBoardName4.text = playerData.first
                            binding.leaderBoardScore4.text = playerData.second.toString()
                        }

                        else -> Toast.makeText(requireContext(), "valeur de leaderboard inconnue", Toast.LENGTH_LONG).show()
                    }
                }


                val scoreboardData = List(game.course!!.numberOfHoles) {
                    index -> Pair(game.course.holes[index].par!!, game.scoreBook[player.name]!![index])
                }

                (binding.scoreboard.adapter as IndividualScoreboardAdapter).updateData(scoreboardData)

            }
        }

    }


}