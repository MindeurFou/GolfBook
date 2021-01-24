package com.example.golfbook.data.remote.game

data class FirestoreGameEntity(
        val courseName: String? = null,
        val courseId: String? = null,
        val scorebook: Map<String, List<Int?>>? = null
)
