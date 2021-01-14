package com.example.golfbook.data.remote

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

object RemoteHomeDataSource {

    private val firestore = Firebase.firestore

    private val playerCollectionRef = firestore.collection("player")
    private val loungeCollectionRef = firestore.collection("lounge")

    //  PEUT RETOURNER LA REF DU PLAYER AJOUTÉ
    suspend fun putPlayer(player: Player) = playerCollectionRef.add(player).await()

    suspend fun updatePlayer(player: Player) {

    }

    suspend fun joinLounge(player: Player, managedPlayers: List<Player>?, lounge: Lounge) : Lounge {

        val loungeQuery = loungeCollectionRef
                .whereEqualTo("loungeNumber", lounge.loungeNumber)
                .get()
                .await()

        if (loungeQuery.documents.size == 1) {

            val loungeId = loungeQuery.documents[0].id

            val newLoungeMap: MutableMap<String, Any> = mutableMapOf() // TODO

            loungeCollectionRef.document(loungeId).set(newLoungeMap, SetOptions.merge())


        } else {
            throw Exception("Query didn't work as expected")
        }

    }

    suspend fun leaveLounge(player: Player, managedPlayers: List<Player>?, lounge: Lounge) : Lounge {

        val loungeQuery = loungeCollectionRef
                .whereEqualTo("loungeNumber", lounge.loungeNumber)
                .get()
                .await()

        if (loungeQuery.documents.size == 1) {

            val loungeId = loungeQuery.documents[0].id

            val newLoungeMap: MutableMap<String, Any> = mutableMapOf() // TODO

            loungeCollectionRef.document(loungeId).set(newLoungeMap, SetOptions.merge())


        } else {
            throw Exception("Query didn't work as expected")
        }

    }

}