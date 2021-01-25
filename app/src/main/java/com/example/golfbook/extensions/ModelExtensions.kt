package com.example.golfbook.extensions

import com.example.golfbook.data.model.Game
import com.example.golfbook.data.model.Hole

object ModelExtensions {

    fun Game.getSortedLeaderboard() : List<Pair<String, Int>> {

        val leaderboardData : MutableList<Pair<String, Int>> = mutableListOf()

        this.players!!.forEach { player ->

            var par = 0
            (this.scoreBook[player.name] ?: error("Le joueur n'as pas été trouvé")).forEach foreach@{

                if (it != null){
                    par += it
                } else
                    return@foreach

            }

            val playerData = Pair(player.name, par)
            leaderboardData.add(playerData)
        }

        leaderboardData.sortBy { it.second }

        return leaderboardData
    }


}