package com.example.golfbook.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.golfbook.model.Game
import com.example.golfbook.model.Hole
import com.example.golfbook.model.Player
import com.example.golfbook.model.Scorebook
import com.example.golfbook.ui.GameViewPagerFragmentArgs
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class GameViewModelFactory(private val args: GameViewPagerFragmentArgs) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")

    }

}

class GameViewModel(args: GameViewPagerFragmentArgs) : ViewModel() {

    private val _scorebook: MutableLiveData<Scorebook> = MutableLiveData()
    val scorebook: LiveData<Scorebook> = _scorebook

    val game: Game = Game(
        players = TODO(),
        course = TODO(),
        scoreBook = Scorebook(),
        currentHole = TODO()
    )

    init {

        _scorebook.value = TODO()
    }



    val liveScoreBook: MutableLiveData<Map<Hole, MutableMap<Player, Int>>> by lazy {
        MutableLiveData<Map<Hole, MutableMap<Player, Int>>>()
    }


}