package com.example.golfbook.ui.game

import androidx.lifecycle.*
import com.example.golfbook.data.model.*
import com.example.golfbook.data.repository.GameRepository
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class GameViewModelFactory(private val args: GameViewPagerFragmentArgs?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")

    }

}

class GameViewModel(
        args: GameViewPagerFragmentArgs?
) : ViewModel() {

    private val gameRepo = GameRepository

    private val _game: MutableLiveData<Resource<Game>> = MutableLiveData()
    val game: LiveData<Resource<Game>> = _game

    private val _initialGame: MutableLiveData<Resource<Game>> = MutableLiveData()
    val intialGame: LiveData<Resource<Game>> = _initialGame


    private val gameListenerRegistration: ListenerRegistration? = args?.let {
        gameRepo.subscribeToGameScorebook(args.gameId) { scorebook ->

            if (_game.value is Resource.Success)
                _game.value = _game.value.apply {
                    (this as Resource.Success).data.scoreBook = scorebook
                }
        }
    }

    // TODO gérer ce bail d'abonnement aux modifications de scorebook. Est ce que la même instance du viewmodel est partagé ?

    init {

        gameRepo.getInitialGame().onEach {
            _initialGame.value = it
        }.launchIn(viewModelScope)

        // loungeRepo.freeLounge()  args = rajouter isAdminPlayer et loungeId  TODO
    }

    fun setGame(game: Resource<Game>) {
        _game.value = game
    }

    override fun onCleared() {
        super.onCleared()
        gameListenerRegistration.remove()
    }
}