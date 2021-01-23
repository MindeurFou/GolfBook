package com.example.golfbook.data.remote.lounge

import com.example.golfbook.data.remote.player.FirestorePlayerEntity

data class FirestoreLoungeEntity(
        val courseName: String? = null,
        val name: String? = null,
        val state: String? = null,
        val playersInLounge: List<FirestorePlayerEntity>? = null
)
