package com.example.golfbook.data.repository

import android.util.Log
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.player.RemotePlayerDataSource
import com.example.golfbook.data.remote.player.RemotePlayerMapper
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.Exception

object PlayerRepository {

    private val remotePlayerDataSource = RemotePlayerDataSource
    private val remoteMapper = RemotePlayerMapper

    fun putPlayer(player: Player) : Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {

            val firestorePlayerEntity = remoteMapper.mapToEntity(player)
            val id = remotePlayerDataSource.putPlayer(firestorePlayerEntity)

            emit(Resource.Success(id))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun getPlayer(playerId : String) : Flow<Resource<Player>> = flow {

        emit(Resource.Loading)

        try {
            val firestorePlayerEntity = remotePlayerDataSource.getPlayer(playerId)

            firestorePlayerEntity?.let {

                val player = remoteMapper.mapFromEntityWithId(firestorePlayerEntity, playerId)
                emit(Resource.Success(player))

            } ?: emit(Resource.Failure(Exception("Le joueur n'as pas été trouvé")))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun updatePlayer(player: Player) : Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {

            val firestorePlayerEntity = remoteMapper.mapToEntity(player)
            val playerId = player.playerId!!

            remotePlayerDataSource.updatePlayer(firestorePlayerEntity, playerId)

            emit(Resource.Success(playerId))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun getManagedPlayersLinkedTo(playerId: String) : Flow<Resource<List<Player>>> = flow {

        emit(Resource.Loading)

        // TODO écrit maintenant mais désactivé tant qu'on arrrive pas jusqu'au finishFragment ou il faudra remove les managedPlayers de firestore

    }

    fun deletePlayer(playerId: String) = CoroutineScope(Dispatchers.IO).launch {

        try {
            remotePlayerDataSource.deletePlayer(playerId)
        }  catch (e: Exception) {
            Log.e("mdebug", e.message.toString())
        }

    }





}