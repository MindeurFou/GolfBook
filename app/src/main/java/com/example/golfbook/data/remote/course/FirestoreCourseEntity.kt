package com.example.golfbook.data.remote.course

data class FirestoreCourseEntity(
        val name: String = "defaultName",
        val numberOfHoles: Int = 18,
        val gamesPlayed: Int = 0,

        val hole1: Int = 3,
        val hole2: Int = 3,
        val hole3: Int = 3,
        val hole4: Int = 3,
        val hole5: Int = 3,
        val hole6: Int = 3,
        val hole7: Int = 3,
        val hole8: Int = 3,
        val hole9: Int = 3,
        val hole10: Int? = 3,
        val hole11: Int? = 3,
        val hole12: Int? = 3,
        val hole13: Int? = 3,
        val hole14: Int? = 3,
        val hole15: Int? = 3,
        val hole16: Int? = 3,
        val hole17: Int? = 3,
        val hole18: Int? = 3,
)