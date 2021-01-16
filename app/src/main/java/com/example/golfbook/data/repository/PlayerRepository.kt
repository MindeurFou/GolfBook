package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.RemotePlayerDataSource
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

object PlayerRepository {

    private val remotePlayerDataSource = RemotePlayerDataSource

    fun putPlayer(player: Player) : Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {
            val id = remotePlayerDataSource.putPlayer(player)
            emit(Resource.Success(id))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun getPlayer(playerId : String) : Flow<Resource<Player?>> = flow {

        emit(Resource.Loading)

        try {
            val player = remotePlayerDataSource.getPlayer(playerId)
            emit(Resource.Success(player))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }


}