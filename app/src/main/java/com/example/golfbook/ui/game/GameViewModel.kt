package com.example.golfbook.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.golfbook.data.model.*
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
        currentHole = TODO()
    )

    init {

        val gameId = args.gameId

        /*game.course = lounge.course as Course

        game.players = lounge.players!!
        game.initScoreBook()

        _scorebook.value = TODO()*/
    }



    val liveScoreBook: MutableLiveData<Map<Hole, MutableMap<Player, Int>>> by lazy {
        MutableLiveData<Map<Hole, MutableMap<Player, Int>>>()
    }


}