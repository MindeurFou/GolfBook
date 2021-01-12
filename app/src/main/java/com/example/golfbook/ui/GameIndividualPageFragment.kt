package com.example.golfbook.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.golfbook.GameViewModel
import com.example.golfbook.R
import com.example.golfbook.model.Player
import com.example.golfbook.ui.adapters.IndividualScoreboardAdapter
import com.example.golfbook.ui.compoundedComponents.LeaderBoardItem
import kotlinx.android.synthetic.main.fragment_game_individual.*
import kotlinx.android.synthetic.main.fragment_game_individual.view.*

class GameIndividualPageFragment(val player: Player) : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    private val players: List<Player> by lazy { viewModel.game!!.players }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_game_individual, container, false)

        // ======== Leaderboard ======
        initLeaderBoard()


        // ======== Avatar ===========
        view.name.text = player.name
        view.imageAvatar.setImageDrawable(requireContext().getDrawable(player.avatarResourceId))



        // ======== Score ===========
        scoreboard.layoutManager = GridLayoutManager(requireContext(), 6, GridLayoutManager.HORIZONTAL, false)
        scoreboard.adapter = IndividualScoreboardAdapter(viewModel.game!!.scoreBook[player]!!)


        viewModel.liveScoreBook.observe(viewLifecycleOwner) { updateScores() }







        return view
    }

    private fun updateScores() {

    }

    private fun initLeaderBoard() {

        leaderBoard.columnCount = players.size

        //players = players.sort()

        for (player in players) {

            val leaderBoardItem = LeaderBoardItem(requireContext())

            leaderBoardItem.name.text = player.name
            leaderBoardItem.par.text = player.par.toString()

            val params = GridLayout.LayoutParams().also {
                it.height = 0
                it.width = 0

                it.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f)
                it.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f)
            }

            leaderBoard.addView(leaderBoardItem, params)

        }
    }
}