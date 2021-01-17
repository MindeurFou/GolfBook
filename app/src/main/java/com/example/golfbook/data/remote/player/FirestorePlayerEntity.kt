package com.example.golfbook.data.remote.player

data class FirestorePlayerEntity (
    var name: String? = null,
    var drawableResourceId: Int = -1,
    val managerId: String? = null
)