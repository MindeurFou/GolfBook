package com.example.golfbook.data.model

data class Game (
        val players: List<Player>?,
        val course: Course?,
        var scoreBook: Map< String, List<Int?> >
    ) {

/*

    fun postScore(hole: Hole, player: Player, score: Int, wantToChange: Boolean) {

        if (score < 1)
            throw IllegalArgumentException("Score can't be less than 1")

        scoreBook!![player]?.let { scoreForHole ->

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
    }*/

}