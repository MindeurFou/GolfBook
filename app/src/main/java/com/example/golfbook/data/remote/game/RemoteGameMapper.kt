package com.example.golfbook.data.remote.game

import com.example.golfbook.data.model.Game
import com.example.golfbook.utils.EntityMapper

object RemoteGameMapper : EntityMapper<FirestoreGameEntity, Game> {

    override fun mapFromEntity(entity: FirestoreGameEntity): Game {
        TODO("Not yet implemented")
    }

    override fun mapToEntity(domainModel: Game): FirestoreGameEntity {
        TODO("Not yet implemented")
    }
}