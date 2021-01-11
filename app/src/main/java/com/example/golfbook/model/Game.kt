package com.example.golfbook.model

import java.lang.Exception
import java.lang.IllegalArgumentException

data class Game (
    val players: List<Player>,
    val course: Course,
    var scoreBook: Map<Hole, MutableMap<Player, Int>>,
    var currentHole: Hole
    ) {


    fun initScoreBook() {

        val _scoreBook: MutableMap<Hole, MutableMap<Player, Int>> = mutableMapOf()

        for (hole in course.holes) {

            val _holeMap: MutableMap<Player, Int> = mutableMapOf()

            for (player in players) {
                _holeMap[player] = -1
            }

            _scoreBook[hole] = _holeMap
        }

        scoreBook = _scoreBook

        currentHole = course.holes[0]
    }

    fun postScore(hole: Hole, player: Player, score: Int, wantToChange: Boolean) {

        if (score < 1)
            throw IllegalArgumentException("Score can't be less than 1")

        scoreBook[hole]?.let { scoreForHole ->

            scoreForHole[player]?.let {

                if (it != -1 && !wantToChange)
                    throw IllegalAccessException("There is already a score here")


            } ?: throw Exception("The player couldn't be find")

            scoreForHole[player]= score
            updateCurrentHole()

        } ?: throw Exception("The hole couldn't be find")
    }

    private fun updateCurrentHole() {

        val holeIsComplete = scoreBook[currentHole]?.all {
            it.value != -1
        }

        holeIsComplete ?: throw Exception("The currentHole couldn't be find")

        if (holeIsComplete)
            currentHole = course.getNextHole(currentHole)
    }

}