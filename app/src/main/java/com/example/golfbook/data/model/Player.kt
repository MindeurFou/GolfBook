package com.example.golfbook.data.model

data class Player(
        var playerId: String? = null,
        var name: String? = null,
        var drawableResourceId: Int = -1,
        val managerId: String? = null
)