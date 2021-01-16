package com.example.golfbook.data.model


data class Course(
    var name: String = "defautlName",
    var numberOfHoles: Int = 18,
    var holes: List<Hole> = listOf(),
    var par: Int = 72,
    var gamesPlayed: Int = 0 // pour trier dans le recyclerView
) {

    fun getNextHole(hole: Hole) : Hole = holes[hole.holeNumber]

}