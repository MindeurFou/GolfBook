package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.LoungeDetails
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.lounge.RemoteLoungeDataSource
import com.example.golfbook.utils.Resource

object LoungeRepository {

    private val remoteLoungeDataSource = RemoteLoungeDataSource

    fun joinLounge(lounge: Lounge, player: Player) = remoteLoungeDataSource.joinLounge(lounge, player)

    fun leaveLounge(lounge: Lounge, player: Player) = remoteLoungeDataSource.leaveLounge(lounge, player)

    fun subscribeToAllLounges(setLiveData: (Resource<List<Lounge>>) -> Unit) = remoteLoungeDataSource.subscribeToAllLounges( setLiveData )

    fun subscribeToALounge(loungeId: String, setLiveData: (Resource<Lounge>) -> Unit ) = remoteLoungeDataSource.subscribeToALounge( setLiveData, loungeId )

    fun subscribeToLoungeDetails(loungeId: String, setLiveData: (Resource<LoungeDetails>) -> Unit) = remoteLoungeDataSource.subscribeToLoungeDetails(loungeId, setLiveData)

    fun updateCourseName(courseName: String, lounge: Lounge) = remoteLoungeDataSource.updateCourseName(courseName, lounge)

    fun startGame(lounge: Lounge, playerName: String) = remoteLoungeDataSource.startGame(lounge, playerName)

    fun acceptStart(lounge: Lounge, playerName: String) = remoteLoungeDataSource.acceptStart(lounge, playerName)

    fun refuseStart(loungeId: String) = remoteLoungeDataSource.refuseStart(loungeId)


}