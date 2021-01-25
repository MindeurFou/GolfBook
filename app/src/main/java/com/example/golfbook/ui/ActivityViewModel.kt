package com.example.golfbook.ui

import androidx.lifecycle.ViewModel
import com.example.golfbook.data.model.Game
import com.example.golfbook.data.model.Player

class ActivityViewModel : ViewModel() {

    var localPlayer: Player? = null
    var game: Game? = null

}