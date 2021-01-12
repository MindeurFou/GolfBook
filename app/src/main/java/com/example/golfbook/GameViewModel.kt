package com.example.golfbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.golfbook.model.Game
import com.example.golfbook.model.Hole
import com.example.golfbook.model.Player

class GameViewModel : ViewModel() {

    val game: Game? = null

    val liveScoreBook: MutableLiveData<Map<Hole, MutableMap<Player, Int>>> by lazy {
        MutableLiveData<Map<Hole, MutableMap<Player, Int>>>()
    }


}