package com.example.golfbook.data.remote.lounge

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.LoungeDetails
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.player.FirestorePlayerEntity
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.SetOptions
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

    fun subscribeToLoungeDetails(
            loungeId: String,
            setLiveData: (Resource<LoungeDetails>) -> Unit) =
            loungeCollectionRef.document(loungeId).collection("loungeDetails").document("loungeDetails").addSnapshotListener { querySnapshot, firestoreException ->

                firestoreException?.let {
                    setLiveData(Resource.Failure(it))
                }

                querySnapshot?.let { documentSnapshot ->

                    val loungeDetails = documentSnapshot.toObject<LoungeDetails>()

                    loungeDetails?.let {
                        setLiveData(Resource.Success(loungeDetails))
                    }

                }
            }

    fun joinLounge(lounge: Lounge, player: Player): Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {

            firestore.runTransaction { transaction ->

                lounge.loungeId ?: throw Exception("Erreur pour récupérer le salon")

                val loungeRef = loungeCollectionRef.document(lounge.loungeId)

                val atomicLounge = transaction.get(loungeRef).toObject<FirestoreLoungeEntity>()?.let { RemoteLoungeMapper.mapFromEntityWithId(it, lounge.loungeId) }

                atomicLounge?.let {

                    val nbPlayer = atomicLounge.playersInLounge?.size ?: 0
                    val state = atomicLounge.state!!

                    if (state != "available")
                        throw Exception("Vous ne pouvez pas rejoindre le salon maintenant")

                    if (nbPlayer == 4)
                        throw Exception("Le salon est déjà complet")

                    val playersInLounge: MutableList<Player> = atomicLounge.playersInLounge?.toMutableList() ?: mutableListOf()

                    playersInLounge.add(player)

                    transaction.update(loungeRef,"playersInLounge", playersInLounge)

                }

                null
            }.await()

            emit(Resource.Success(lounge.loungeId!!))

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

    fun updateCourseName(courseName: String, lounge: Lounge) : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        try {

            lounge.loungeId ?: throw Exception("Erreur pour récupérer le salon")

            loungeCollectionRef.document(lounge.loungeId).update("courseName", courseName).await()

            emit(Resource.Success(Unit))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun startGame(lounge: Lounge, playerName: String) : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        try {

            firestore.runTransaction { transaction ->

                lounge.loungeId ?: throw Exception("Erreur pour récupérer le salon")

                val loungeRef = loungeCollectionRef.document(lounge.loungeId)
                val loungeDetailsRef = loungeRef.collection("loungeDetails").document("loungeDetails")

                val atomicLounge = transaction.get(loungeRef).toObject<FirestoreLoungeEntity>()?.let { RemoteLoungeMapper.mapFromEntityWithId(it, lounge.loungeId) }

                atomicLounge?.let {

                    if (atomicLounge.state == "available") {
                        transaction.update(loungeRef,"state", "starting")
                        transaction.update(loungeDetailsRef, "playersReady", listOf(playerName))
                        transaction.update(loungeDetailsRef, "adminPlayer", playerName)
                    } else
                        throw Exception("La partie est déjà en train de commencer")

                }

            }.await()

            emit(Resource.Success(Unit))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }


    }

    @Suppress("UNCHECKED_CAST")
    fun acceptStart(lounge: Lounge, playerName: String) : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        try {

            firestore.runTransaction { transaction ->

                lounge.loungeId ?: throw Exception("Erreur pour récupérer le salon")

                val loungeRef = loungeCollectionRef.document(lounge.loungeId)
                val playersReadyRef = loungeRef.collection("loungeDetails").document("loungeDetails")

                val atomicLounge = transaction.get(loungeRef).toObject<FirestoreLoungeEntity>()?.let { RemoteLoungeMapper.mapFromEntityWithId(it, lounge.loungeId) }
                val playersReady = transaction.get(playersReadyRef)["playersReady"] as MutableList<String>


                atomicLounge?.let {

                    if (atomicLounge.state == "starting") {

                        playersReady.add(playerName)
                        transaction.update(playersReadyRef, "playersReady", playersReady)
                    } else
                        throw Exception("Impossible de rejoindre la partie")

                }

            }.await()

            emit(Resource.Success(Unit))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }


    }

    fun refuseStart(loungeId: String) {

        val loungeDocumentRef = loungeCollectionRef.document(loungeId)
        val loungeDetailsDocumentRef = loungeDocumentRef.collection("loungeDetails").document("loungeDetails")

        firestore.runBatch { batch ->

            batch.update(loungeDocumentRef,"state", "available")

            batch.update(loungeDetailsDocumentRef, "adminPlayer", "")
            batch.update(loungeDetailsDocumentRef, "playersReady", listOf<String>())

        }
    }


    fun setGameId(loungeId: String, gameId: String) {

        val loungeDetailsDocumentRef = loungeCollectionRef.document(loungeId).collection("loungeDetails").document("loungeDetails")

        loungeDetailsDocumentRef.update("gameId", gameId)
    }


    fun freeLounge(loungeId: String) {

        val mapLounge = mapOf("courseName" to "", "playersInLounge" to listOf<FirestorePlayerEntity>(), "state" to "available")
        loungeCollectionRef.document(loungeId).set(mapLounge, SetOptions.merge())

        val mapLoungeDetails = mapOf("adminPlayer" to "", "playersReady" to listOf<String>(), "gameId" to "")
        loungeCollectionRef.document(loungeId).collection("loungeDetails").document("loungeDetails").set(mapLoungeDetails)

    }


}