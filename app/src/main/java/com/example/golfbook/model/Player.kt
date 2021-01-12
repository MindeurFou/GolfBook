package com.example.golfbook.model

data class Player(
    var name: String?,
    var avatarResourceId: Int,
    var par: Int? = null
)