package com.example.golfbook.model

data class Scorebook(
    var scoreBook: Map<Player, MutableMap<Hole, Int>>
)