package com.example.golfbook.data.remote

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object RemoteLoungeDataSource {

    private val firestore = Firebase.firestore

    private val loungeCollectionRef = firestore.collection("lounge")


    suspend fun joinLounge() {

       // loungeCollectionRef.add().await()
    }

    suspend fun leaveLounge() : Lounge {

        /*val loungeQuery = loungeCollectionRef
                .whereEqualTo("loungeNumber", lounge.loungeNumber)
                .get()
                .await()

        if (loungeQuery.documents.size == 1) {

            val loungeId = loungeQuery.documents[0].id

            val newLoungeMap: MutableMap<String, Any> = mutableMapOf() // TODO

            loungeCollectionRef.document(loungeId).set(newLoungeMap, SetOptions.merge())


        } else {
            throw Exception("Query didn't work as expected")
        }*/


        return Lounge(1)

    }

}