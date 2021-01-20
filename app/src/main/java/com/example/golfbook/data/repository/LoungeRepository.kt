package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.remote.lounge.RemoteLoungeDataSource
import com.example.golfbook.data.remote.lounge.RemoteLoungeMapper
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.*
import java.lang.Exception

object LoungeRepository {

    private val remoteLoungeDataSource = RemoteLoungeDataSource
    private val remoteLoungeMapper = RemoteLoungeMapper

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

    suspend fun getRealTimeUpdatesOfLounges() : Flow<Resource<List<Lounge>>>  = remoteLoungeDataSource.getRealTimeUpdatesOfLounges()


    fun subscribe(setLiveData: (Resource<List<Lounge>>) -> Unit) = remoteLoungeDataSource.subscribe( setLiveData )


}