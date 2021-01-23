package com.example.golfbook.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.golfbook.data.model.*
import com.example.golfbook.data.repository.GameRepository
import com.example.golfbook.data.repository.LoungeRepository
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.ListenerRegistration
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

class GameViewModel(
        args: GameViewPagerFragmentArgs
) : ViewModel() {

    private val gameRepo = GameRepository
    private val loungeRepo = LoungeRepository

    private val _scorebook: MutableLiveData<Scorebook> = MutableLiveData()
    val scorebook: LiveData<Scorebook> = _scorebook

    private val _createGameState: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val createGameState: LiveData<Resource<Unit>> = _createGameState

    private val _lounge: MutableLiveData<Resource<Lounge>> = MutableLiveData(Resource.Loading)
    val lounge: LiveData<Resource<Lounge>> = _lounge

    private val _game: MutableLiveData<Resource<Game>> = MutableLiveData()
    val game: LiveData<Resource<Game>> = _game


    private val loungeListenerRegistration = loungeRepo.subscribeToALounge(args.loungeId) { loungeResource ->
        _lounge.value = loungeResource

       // if (loungeResource contains la game)
    }

    private lateinit var gameListenerRegistration: ListenerRegistration


    val liveScoreBook: MutableLiveData<Map<Hole, MutableMap<Player, Int>>> by lazy {
        MutableLiveData<Map<Hole, MutableMap<Player, Int>>>()
    }





    init {

        if (args.localPlayerIsAdmin) {
            gameRepo.createGame(args.loungeId)
        }
    }

    fun subscribeToGame(gameId: String) {

        gameListenerRegistration = gameRepo.subscribeToGame(gameId) {
            _game.value = it
        }
    }




}