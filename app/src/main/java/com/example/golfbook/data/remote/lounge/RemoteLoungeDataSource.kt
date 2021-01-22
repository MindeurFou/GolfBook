package com.example.golfbook.data.remote.lounge

import android.util.Log
import android.widget.Toast
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.Exception


object RemoteLoungeDataSource {

    private val firestore = Firebase.firestore

    private val loungeCollectionRef = firestore.collection("lounge")

    private val remoteLoungeMapper = RemoteLoungeMapper


    fun subscribeToAllLounges(setLiveData: (Resource<List<Lounge>>) -> Unit) = loungeCollectionRef.addSnapshotListener { querySnapshot, firestoreException ->

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

    fun subscribeToALounge(setLiveData: (Resource<Lounge>) -> Unit, loungeId: String) = loungeCollectionRef.document(loungeId).addSnapshotListener { querysnapshot, firestoreException ->

        firestoreException?.let {
            setLiveData(Resource.Failure(it))
        }

        querysnapshot?.let { documentSnapshot ->

            val firestoreLoungeEntity = documentSnapshot.toObject<FirestoreLoungeEntity>()

            firestoreLoungeEntity?.let {
                val lounge = remoteLoungeMapper.mapFromEntityWithId(it, documentSnapshot.id)
                setLiveData(Resource.Success(lounge))
            }

        }


    }



    fun joinLounge(lounge: Lounge, player: Player): Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {

            lounge.loungeId ?: throw Exception("Erreur pour récupérer le salon")

            val nbPlayer = lounge.playersInLounge?.size

            nbPlayer?.let {

                if (it < 4 ) {

                    val playersInLounge: MutableList<Player> = lounge.playersInLounge?.toMutableList() ?: mutableListOf()

                    playersInLounge.add(player)

                    loungeCollectionRef.document(lounge.loungeId).update(mapOf("playersInLounge" to playersInLounge)).await()

                    emit(Resource.Success(lounge.loungeId))
                } else
                    throw Exception("Le salon est déjà plein")

            } ?: throw Exception("Erreur pour récupérer le salon")


        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun leaveLounge(lounge: Lounge, player: Player) : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        try {

            lounge.loungeId ?: throw Exception("Erreur pour récupérer le salon")

            if (lounge.playersInLounge!!.any { it == player }) {

                val playerInLounge = lounge.playersInLounge!!.toMutableList()

                if (playerInLounge.remove(player)) {
                    loungeCollectionRef.document(lounge.loungeId).update("playersInLounge", playerInLounge).await()
                    emit(Resource.Success(Unit))
                }
            }

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }


}