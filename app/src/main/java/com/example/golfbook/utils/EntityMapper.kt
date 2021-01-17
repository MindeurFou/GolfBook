package com.example.golfbook.utils

interface EntityMapper <Entity, DomainModel> {

    fun mapFromEntity(entity: Entity, entityId: String) : DomainModel

    fun mapToEntity(domainModel: DomainModel) : Entity
}