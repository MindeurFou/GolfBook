package com.example.golfbook.data.model

data class Scorebook(
    var scoreBook: Map<Player, MutableMap<Hole, Int>>
)