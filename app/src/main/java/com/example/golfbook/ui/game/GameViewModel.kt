package com.example.golfbook.ui.game

import androidx.lifecycle.*
import com.example.golfbook.data.model.*
import com.example.golfbook.data.repository.GameRepository
import com.example.golfbook.data.repository.LoungeRepository
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class GameViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel() as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")

    }

}

class GameViewModel() : ViewModel() {

    private val gameRepo = GameRepository
    private val loungeRepo = LoungeRepository

    private lateinit var gameId: String

    private val _game: MutableLiveData<Resource<Game>> = MutableLiveData()
    val game: LiveData<Resource<Game>> = _game

    private val _initialGame: MutableLiveData<Resource<Game>> = MutableLiveData()
    val intialGame: LiveData<Resource<Game>> = _initialGame


    private var gameListenerRegistration: ListenerRegistration? = null

    fun initGame(gameId: String) {

        this.gameId = gameId

        gameRepo.getInitialGame(gameId).onEach {
            _initialGame.value = it
        }.launchIn(viewModelScope)

    }

    fun subscribeToScorebook(gameId: String) {

        gameListenerRegistration = gameRepo.subscribeToGameScorebook(gameId) { scorebook ->

            if (_game.value is Resource.Success)
                _game.value = _game.value.apply {
                    (this as Resource.Success).data.scoreBook = scorebook
                }
        }

    }

    fun setGame(game: Resource<Game>) {
        _game.value = game
    }

    fun freeLounge(loungeId: String) {
        loungeRepo.freeLounge(loungeId)
    }

    fun putScore(gameId: String, playerName: String, index: Int, score: Int) = gameRepo.putScore(gameId, playerName, index, score)

    override fun onCleared() {
        super.onCleared()
        gameListenerRegistration?.remove()
    }
}