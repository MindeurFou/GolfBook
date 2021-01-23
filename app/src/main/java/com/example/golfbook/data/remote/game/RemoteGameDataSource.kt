package com.example.golfbook.data.remote.game

import com.example.golfbook.data.model.Game
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object RemoteGameDataSource {

    private val firestore = Firebase.firestore

    private lateinit var gameRef: DocumentReference

    private val remoteGameMapper = RemoteGameMapper

    fun createGame(loungeId: String) : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        try {


        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


    fun subscribeToGame(gameId: String, setLiveData: (Resource<Game>) -> Unit) : ListenerRegistration {

        gameRef = firestore.document("").collection("game").document(gameId)

        return gameRef.addSnapshotListener { querySnapshot, firestoreException ->

            firestoreException?.let {
                setLiveData(Resource.Failure(it))
            }

            try {



            } catch (e: Exception) {
                setLiveData(Resource.Failure(e))
            }
        }

    }


}