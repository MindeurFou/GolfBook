package com.example.golfbook.model

data class Course(
    var name: String,
    var numberOfHoles: Int,
    var holes: List<Hole>,
    var par: Int,
    var gamesPlayed: Int // pour trier dans le recyclerView
) {

    fun getNextHole(hole: Hole) : Hole = holes[hole.holeNumber]

}