package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.RemoteLoungeDataSource
import com.example.golfbook.data.remote.RemotePlayerDataSource
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

object LoungeRepository {

    private val remoteLoungeDataSource = RemoteLoungeDataSource

    suspend fun joinLounge() : Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        try {
            val updatedLounge = remoteLoungeDataSource.joinLounge()
            emit(Resource.Success(updatedLounge))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    suspend fun leaveLounge() : Flow<Resource<Lounge>> = flow {

        emit(Resource.Loading)

        try {
            val updatedLounge = remoteLoungeDataSource.leaveLounge()
            emit(Resource.Success(updatedLounge))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }



}