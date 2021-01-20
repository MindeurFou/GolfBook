package com.example.golfbook.data.remote.lounge

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


object RemoteLoungeDataSource {

    private val firestore = Firebase.firestore

    private val loungeCollectionRef = firestore.collection("lounge")

    private val remoteLoungeMapper = RemoteLoungeMapper

    suspend fun getRealTimeUpdatesOfLounges() : Flow<Resource<List<Lounge>>> = flow {

        emit(Resource.Loading)

        try {




        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }


    fun subscribe(setLiveData: (Resource<List<Lounge>>) -> Unit) = loungeCollectionRef.addSnapshotListener { querySnapshot, firestoreException ->

            firestoreException?.let {
                setLiveData(Resource.Failure(it))
            }

            querySnapshot?.let {

                val listLounges: MutableList<Lounge> = mutableListOf()

                for (document in it) {

                    val firestoreLoungeEntity = document.toObject<FirestoreLoungeEntity>()

                    listLounges.add(remoteLoungeMapper.mapFromEntityWithId(firestoreLoungeEntity, document.id))

                }

                setLiveData(Resource.Success(listLounges))
            }

        }



    suspend fun joinLounge() {

        loungeCollectionRef.addSnapshotListener { value, error ->

            value?.documents?.get(1)?.get("playersInLounge")
        }

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


       return Lounge()

    }

    suspend fun testLounge() {

        val query = loungeCollectionRef.get().await()

        for (document in query.documents) {
            Log.d("mdebug", document.data.toString())
        }


    }

    suspend fun testLoungeWrite(firestoreLoungeEntity: FirestoreLoungeEntity) {

        loungeCollectionRef.add(firestoreLoungeEntity).await()



    }

}