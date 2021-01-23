package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Game
import com.example.golfbook.data.remote.game.RemoteGameDataSource
import com.example.golfbook.utils.Resource

object GameRepository {

    private val remoteGameDataSource = RemoteGameDataSource

    fun createGame(loungeId: String) = remoteGameDataSource.createGame(loungeId)

    fun subscribeToGame(gameId: String, setLiveData: (Resource<Game>) -> Unit) = remoteGameDataSource.subscribeToGame(gameId, setLiveData)
}