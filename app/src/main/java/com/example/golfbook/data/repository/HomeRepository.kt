package com.example.golfbook.data.repository

import androidx.compose.runtime.emit
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.RemoteHomeDataSource
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

object HomeRepository {

    private val remoteDataSource = RemoteHomeDataSource

    suspend fun putPlayer(player: Player) : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        delay(1000)

        try {
            remoteDataSource.putPlayer(player)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }


    suspend fun joinLounge(player: Player, managedPlayers: List<Player>?, lounge: Lounge) : Flow<Resource<Lounge>> = flow {

        emit(Resource.Loading)

        try {
            val updatedLounge = remoteDataSource.joinLounge(player, managedPlayers, lounge)
            emit(Resource.Success(updatedLounge))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    suspend fun leaveLounge(player: Player, managedPlayers: List<Player>?, lounge: Lounge) : Flow<Resource<Lounge>> = flow {

        emit(Resource.Loading)

        try {
            val updatedLounge = remoteDataSource.joinLounge(player, managedPlayers, lounge)
            emit(Resource.Success(updatedLounge))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }



}