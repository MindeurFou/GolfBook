package com.example.golfbook.data.model

data class Lounge(
        val loungeNumber: Int,
        var players: List<Player>? = null,
        var course: String? = null
)