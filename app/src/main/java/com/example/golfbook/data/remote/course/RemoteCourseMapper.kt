package com.example.golfbook.data.remote.course

import com.example.golfbook.data.model.Course
import com.example.golfbook.data.model.Hole
import com.example.golfbook.utils.EntityMapper

object RemoteCourseMapper : EntityMapper<FirestoreCourseEntity, Course> {

    override fun mapFromEntity(entity: FirestoreCourseEntity, entityId: String): Course {

        val listHoles: MutableList<Hole> = mutableListOf()
        val numberOfHoles = entity.numberOfHoles

        listHoles.add(Hole(1, entity.hole1))
        listHoles.add(Hole(2, entity.hole2))
        listHoles.add(Hole(3, entity.hole3))
        listHoles.add(Hole(4, entity.hole4))
        listHoles.add(Hole(5, entity.hole5))
        listHoles.add(Hole(6, entity.hole6))
        listHoles.add(Hole(7, entity.hole7))
        listHoles.add(Hole(8, entity.hole8))
        listHoles.add(Hole(9, entity.hole9))

        if (numberOfHoles == 18) {
            listHoles.add(Hole(10, entity.hole10!!))
            listHoles.add(Hole(11, entity.hole11!!))
            listHoles.add(Hole(12, entity.hole12!!))
            listHoles.add(Hole(13, entity.hole13!!))
            listHoles.add(Hole(14, entity.hole14!!))
            listHoles.add(Hole(15, entity.hole15!!))
            listHoles.add(Hole(16, entity.hole16!!))
            listHoles.add(Hole(17, entity.hole17!!))
            listHoles.add(Hole(18, entity.hole18!!))
        }

        var par = 0

        for (hole in listHoles)
            par += hole.par

        return Course(entity.name, numberOfHoles, listHoles, par)
    }

    override fun mapToEntity(domainModel: Course): FirestoreCourseEntity {

        return FirestoreCourseEntity(
                name = domainModel.name,
                numberOfHoles = domainModel.numberOfHoles,
                gamesPlayed = domainModel.gamesPlayed,
                domainModel.holes[0].par,
                domainModel.holes[1].par,
                domainModel.holes[2].par,
                domainModel.holes[3].par,
                domainModel.holes[4].par,
                domainModel.holes[5].par,
                domainModel.holes[6].par,
                domainModel.holes[7].par,
                domainModel.holes[8].par,
                domainModel.holes.getOrNull(9)?.par,
                domainModel.holes.getOrNull(10)?.par,
                domainModel.holes.getOrNull(11)?.par,
                domainModel.holes.getOrNull(12)?.par,
                domainModel.holes.getOrNull(13)?.par,
                domainModel.holes.getOrNull(14)?.par,
                domainModel.holes.getOrNull(15)?.par,
                domainModel.holes.getOrNull(16)?.par,
                domainModel.holes.getOrNull(17)?.par,
        )
    }

    fun mapFromEntityList(entities: List<Pair<String, FirestoreCourseEntity>>): List<Course>{
        return entities.map { mapFromEntity(it.second, it.first) }
    }
}