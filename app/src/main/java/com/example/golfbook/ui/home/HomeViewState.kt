package com.example.golfbook.ui.home

import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player

data class HomeViewState(
        val player: Player,
        val managedPlayers: List<Player>?,
        var lounges: List<Lounge>
)