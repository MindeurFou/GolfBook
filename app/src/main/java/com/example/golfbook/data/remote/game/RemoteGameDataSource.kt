package com.example.golfbook.data.remote.game

import com.example.golfbook.data.model.Course
import com.example.golfbook.data.model.Game
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.course.FirestoreCourseEntity
import com.example.golfbook.data.remote.course.RemoteCourseMapper
import com.example.golfbook.data.remote.lounge.FirestoreLoungeEntity
import com.example.golfbook.data.remote.lounge.RemoteLoungeMapper
import com.example.golfbook.data.remote.player.FirestorePlayerEntity
import com.example.golfbook.data.remote.player.RemotePlayerMapper
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

object RemoteGameDataSource {

    private val firestore = Firebase.firestore

    private val gameCollectionRef = firestore.collection("game")

    private val remoteLoungeMapper = RemoteLoungeMapper
    private val remoteCourseMapper = RemoteCourseMapper
    private val remotePlayerMapper = RemotePlayerMapper

    // renvoie le gameId
    fun createGame(loungeId: String) : Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {

            val loungeDocumentRef = firestore.collection("lounge").document(loungeId)
            val courseCollectionRef = firestore.collection("course")

            val firestoreLoungeEntity = loungeDocumentRef.get().await().toObject<FirestoreLoungeEntity>()

            firestoreLoungeEntity?.let {

                val lounge = remoteLoungeMapper.mapFromEntity(firestoreLoungeEntity)

                val courseName = lounge.courseName!!

                val querySnapshot = courseCollectionRef.whereEqualTo("name", courseName).get().await()

                if (querySnapshot.documents.size == 1) {

                    val firestoreCourseEntity = querySnapshot.documents[0].toObject<FirestoreCourseEntity>()


                    val res = firestoreCourseEntity?.let {

                        val course = remoteCourseMapper.mapFromEntity(it)

                        val players = lounge.playersInLounge!!

                        val scorebook: MutableMap<String, List<Int?>> = mutableMapOf()

                        for (player in players) {

                            val score: List<Int?> = List(course.numberOfHoles) {
                                null
                            }

                            scorebook.put(player.name, score)
                        }


                        val firestoreGameEntity = FirestoreGameEntity(
                                courseName = courseName,
                                courseId = querySnapshot.documents[0].id,
                                scorebook = scorebook
                        )

                        val gameDocument = firestore.collection("game").document()

                        gameDocument.set(firestoreGameEntity).await()

                        loungeDocumentRef.update("state", "busy").await()

                        emit(Resource.Success(gameDocument.id))

                    } ?: throw Exception("Erreur pour récupérer le parcours")

                } else
                    throw Exception("Erreur pour récupérer le parcours")


            } ?: throw Exception("Impossible de trouver le salon")



        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


    fun subscribeToGame(gameId: String, setLiveData: (Map<String, List<Int?>>) -> Unit)  = gameCollectionRef.document(gameId).addSnapshotListener { querySnapshot, _ ->

            querySnapshot?.let {

                val firestoreGameEntity = it.toObject<FirestoreGameEntity>()

                firestoreGameEntity?.let {
                    firestoreGameEntity.scorebook?.let(setLiveData)
                }
            }
    }



    fun getInitialGame(gameId: String) : Flow<Resource<Game>> = flow {

        emit(Resource.Loading)

        try {

            val gameDoc = gameCollectionRef.document(gameId).get().await()

            val firestoreGameEntity = gameDoc.toObject<FirestoreGameEntity>()

            firestoreGameEntity?.let {

                var course: Course? = null

                val firestoreCourseEntity = firestore.collection("course").document(firestoreGameEntity.courseId!!).get().await().toObject<FirestoreCourseEntity>()

                firestoreCourseEntity?.let {
                     course = remoteCourseMapper.mapFromEntity(firestoreCourseEntity)
                }

                val players: MutableList<Player> = mutableListOf()

                firestoreGameEntity.scorebook!!.forEach { (name, _) ->

                    val querySnapshot = firestore.collection("player").whereEqualTo("name", name).get().await()

                    if (querySnapshot.documents.size == 1) {

                        val firestorePlayerEntity = querySnapshot.documents[0].toObject<FirestorePlayerEntity>()

                        firestorePlayerEntity?.let {

                            val player = remotePlayerMapper.mapFromEntityWithId(firestorePlayerEntity, querySnapshot.documents[0].id)
                            players.add(player)

                        }

                    } else {
                        emit(Resource.Failure(Exception("Impossible de retrouver un joueur dans la partie")))
                    }

                }

                if (course != null && players.isNotEmpty()) {

                    val game = Game(
                            players = players,
                            course = course!!,
                            scoreBook = firestoreGameEntity.scorebook
                    )

                    emit(Resource.Success(game))

                } else {
                    throw Exception("Problèmes pour récupérer la partie")
                }


            }

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun putScore(gameId: String) {

    }


}