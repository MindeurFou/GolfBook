package com.example.golfbook.data.remote.lounge

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.player.RemotePlayerMapper
import com.example.golfbook.utils.EntityMapper

object RemoteLoungeMapper : EntityMapper<FirestoreLoungeEntity, Lounge> {

    private val remotePlayerMapper = RemotePlayerMapper

    override fun mapFromEntity(entity: FirestoreLoungeEntity): Lounge {
        return Lounge()
    }

    override fun mapToEntity(domainModel: Lounge): FirestoreLoungeEntity {
        return FirestoreLoungeEntity()
    }

    fun mapFromEntityWithId(entity: FirestoreLoungeEntity, loungeId: String) : Lounge {

        var listPlayersInLounge: MutableList<Player>? = null

        entity.playersInLounge?.let {

            listPlayersInLounge = mutableListOf()

            for (playerEntity in it) {
                listPlayersInLounge!!.add(remotePlayerMapper.mapFromEntity(playerEntity))
            }
        }

        var listPlayersReady: MutableList<Player>? = null

        entity.playersReady?.let {

            listPlayersReady = mutableListOf()

            for (name in it){

                val player = findPlayerInListByName(name, listPlayersInLounge!!)
                if (player != null) {
                    listPlayersReady!!.add(player)
                }
            }
        }


        return Lounge(
                loungeId = loungeId,
                name = entity.name,
                playersInLounge = listPlayersInLounge,
                playersReady = listPlayersReady,
                state = entity.state,
                courseName = entity.courseName
        )

    }

    private fun findPlayerInListByName(name: String, list: List<Player>) : Player? {

        for (player in list) {
            if (player.name!! == name) {
                return player
            }
        }

        return null
    }


}