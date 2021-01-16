package com.example.golfbook.data.remote

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object RemotePlayerDataSource {

    private val firestore = Firebase.firestore

    private val playerCollectionRef = firestore.collection("player")


    suspend fun putPlayer(player: Player) : String = playerCollectionRef.add(player).await().id

    suspend fun getPlayer(playerId: String) : Player?  {
        val document = playerCollectionRef.document(playerId).get().await()
        return document.toObject()
    }

    suspend fun updatePlayer(player: Player) {

    }



}