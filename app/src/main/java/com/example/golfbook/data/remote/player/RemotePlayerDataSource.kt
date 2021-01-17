package com.example.golfbook.data.remote.player

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object RemotePlayerDataSource {

    private val firestore = Firebase.firestore

    private val playerCollectionRef = firestore.collection("player")


    suspend fun putPlayer(firestorePlayerEntity: FirestorePlayerEntity) : String = playerCollectionRef.add(firestorePlayerEntity).await().id

    suspend fun getPlayer(playerId: String) : FirestorePlayerEntity?  {
        val document = playerCollectionRef.document(playerId).get().await()
        return document.toObject()
    }

    suspend fun updatePlayer(firestorePlayerEntity: FirestorePlayerEntity, playerId: String) = playerCollectionRef.document(playerId)
            .set(firestorePlayerEntity)
            .await()


    suspend fun deletePlayer(playerId: String) = playerCollectionRef.document(playerId).delete().await()

}