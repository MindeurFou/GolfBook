package com.example.golfbook.data.repository

import com.example.golfbook.data.remote.game.RemoteGameDataSource

object GameRepository {

    private val remoteGameDataSource = RemoteGameDataSource

    fun createGame(loungeId: String) = remoteGameDataSource.createGame(loungeId)

    fun subscribeToGameScorebook( gameId: String, setLiveData: (Map<String, List<Int?>>) -> Unit) = remoteGameDataSource.subscribeToGame(gameId, setLiveData)

    fun getInitialGame(gameId: String) = remoteGameDataSource.getInitialGame(gameId)

    fun putScore(gameId: String) = remoteGameDataSource.putScore(gameId)
}