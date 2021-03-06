package com.example.golfbook.data.remote.lounge

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.player.RemotePlayerMapper
import com.example.golfbook.utils.EntityMapper

object RemoteLoungeMapper : EntityMapper<FirestoreLoungeEntity, Lounge> {

    private val remotePlayerMapper = RemotePlayerMapper

    override fun mapFromEntity(entity: FirestoreLoungeEntity): Lounge {

        var listPlayersInLounge: MutableList<Player>? = null

        entity.playersInLounge?.let {

            listPlayersInLounge = mutableListOf()

            for (playerEntity in it) {
                listPlayersInLounge!!.add(remotePlayerMapper.mapFromEntity(playerEntity))
            }
        }

        return Lounge(
                loungeId = null,
                name = entity.name,
                playersInLounge = listPlayersInLounge,
                state = entity.state,
                courseName = entity.courseName
        )

    }

    override fun mapToEntity(domainModel: Lounge): FirestoreLoungeEntity {

        val playersInLounge = domainModel.playersInLounge!!.map { remotePlayerMapper.mapToEntity(it) }

        return FirestoreLoungeEntity(
                courseName = domainModel.courseName,
                name = domainModel.courseName,
                state = domainModel.state,
                playersInLounge = playersInLounge
        )
    }

    fun mapFromEntityWithId(entity: FirestoreLoungeEntity, loungeId: String) : Lounge {

        var listPlayersInLounge: MutableList<Player>? = null

        entity.playersInLounge?.let {

            listPlayersInLounge = mutableListOf()

            for (playerEntity in it) {
                listPlayersInLounge!!.add(remotePlayerMapper.mapFromEntity(playerEntity))
            }
        }

        return Lounge(
                loungeId = loungeId,
                name = entity.name,
                playersInLounge = listPlayersInLounge,
                state = entity.state,
                courseName = entity.courseName
        )

    }

}