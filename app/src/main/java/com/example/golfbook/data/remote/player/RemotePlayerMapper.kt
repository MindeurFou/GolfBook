package com.example.golfbook.data.remote.player

import com.example.golfbook.data.model.Player
import com.example.golfbook.utils.EntityMapper

object RemotePlayerMapper : EntityMapper<FirestorePlayerEntity, Player> {

    override fun mapFromEntity(entity: FirestorePlayerEntity, entityId: String): Player = Player(
            playerId = entityId,
            name = entity.name,
            drawableResourceId = entity.drawableResourceId,
            managerId = entity.managerId
    )



    override fun mapToEntity(domainModel: Player): FirestorePlayerEntity = FirestorePlayerEntity(
            name = domainModel.name,
            drawableResourceId = domainModel.drawableResourceId,
            managerId = domainModel.managerId,
    )
}