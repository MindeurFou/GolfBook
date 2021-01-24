package com.example.golfbook.data.remote.player

import com.example.golfbook.R
import com.example.golfbook.data.model.Player
import com.example.golfbook.utils.EntityMapper

object RemotePlayerMapper : EntityMapper<FirestorePlayerEntity, Player> {

    override fun mapFromEntity(entity: FirestorePlayerEntity): Player {

        val drawableResourceId = if (entity.drawableResourceId == -1)
            R.drawable.man1
        else
            entity.drawableResourceId

        return Player(
                playerId = null,
                name = entity.name!!,
                drawableResourceId = drawableResourceId,
                managerId = entity.managerId
        )
    }

    fun mapFromEntityWithId(entity: FirestorePlayerEntity, entityId: String) : Player {

        val drawableResourceId = if (entity.drawableResourceId == -1)
            R.drawable.man1
        else
            entity.drawableResourceId

        return Player(
                playerId = entityId,
                name = entity.name!!,
                drawableResourceId = drawableResourceId,
                managerId = entity.managerId
        )
    }



    override fun mapToEntity(domainModel: Player): FirestorePlayerEntity = FirestorePlayerEntity(
            name = domainModel.name,
            drawableResourceId = domainModel.drawableResourceId,
            managerId = domainModel.managerId,
    )
}