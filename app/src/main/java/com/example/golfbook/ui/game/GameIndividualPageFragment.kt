package com.example.golfbook.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golfbook.databinding.FragmentGameIndividualBinding
import com.example.golfbook.data.model.Player
import com.example.golfbook.ui.adapters.IndividualScoreboardAdapter


class GameIndividualPageFragment(val player: Player) : Fragment() {

    private lateinit var binding: FragmentGameIndividualBinding

    //private val args: GameIndividualPageFragmentArgs by navArgs()

    private val viewModel: GameViewModel by viewModels()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentGameIndividualBinding.inflate(inflater)

        // ======== Leaderboard ======
        initLeaderBoard()


        // ======== Avatar ===========
        binding.name.text = player.name
        binding.imageAvatar.setImageDrawable(requireContext().getDrawable(player.drawableResourceId))



        // ======== Score ===========
        binding.scoreboard.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.scoreboard.adapter = IndividualScoreboardAdapter(viewModel.game.scoreBook!![player]!!)


        viewModel.liveScoreBook.observe(viewLifecycleOwner) { updateScores() }



        return binding.root
    }

    private fun updateScores() {

    }

    private fun initLeaderBoard() {

        //binding.leaderBoard.columnCount = players.size

        //players = players.sort()

        /*for (player in players) {

            val leaderBoardItem = LeaderBoardItem(requireContext())

            leaderBoardItem.name.text = player.name
            leaderBoardItem.par.text = player.par.toString()

            val params = GridLayout.LayoutParams().also {
                it.height = 0
                it.width = 0

                it.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f)
                it.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f)
            }

            binding.leaderBoard.addView(leaderBoardItem, params)

        }*/
    }
}