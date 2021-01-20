package com.example.golfbook.data.model

data class Lounge(
        val loungeId: String? = null,
        val name: String? = null,
        var playersInLounge: List<Player>? = null,
        var playersReady: List<Player>? = null,
        var state: String? = null,
        var courseName: String? = null
)