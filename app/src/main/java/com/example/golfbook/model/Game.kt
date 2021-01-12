package com.example.golfbook.model

import java.lang.Exception
import java.lang.IllegalArgumentException

data class Game (
    val players: List<Player>,
    val course: Course,
    var scoreBook: Map<Player, MutableMap<Hole, Int>>,
    var currentHole: Hole
    ) {


    fun initScoreBook() {

        val _scoreBook: MutableMap<Player, MutableMap<Hole, Int>> = mutableMapOf()

        for (player in players) {

            val _holeMap: MutableMap<Hole, Int> = mutableMapOf()

            for (Hole in course.holes) {
                _holeMap[Hole] = -1
            }

            _scoreBook[player] = _holeMap
        }

        scoreBook = _scoreBook

        currentHole = course.holes[0]
    }

    fun postScore(hole: Hole, player: Player, score: Int, wantToChange: Boolean) {

        if (score < 1)
            throw IllegalArgumentException("Score can't be less than 1")

        scoreBook[player]?.let { scoreForHole ->

            scoreForHole[hole]?.let {

                if (it != -1 && !wantToChange)
                    throw IllegalAccessException("There is already a score here")


            } ?: throw Exception("The hole couldn't be find")

            scoreForHole[hole]= score
            updateCurrentHole()

        } ?: throw Exception("The player couldn't be find")
    }

    private fun updateCurrentHole() {

        /*val holeIsComplete = scoreBook[currentHole]?.all {
            it.value != -1
        }

        holeIsComplete ?: throw Exception("The currentHole couldn't be find")

        if (holeIsComplete)
            currentHole = course.getNextHole(currentHole)*/
    }

}