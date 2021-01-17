package com.example.golfbook.ui.home


import androidx.lifecycle.*
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.repository.LoungeRepository
import com.example.golfbook.data.repository.PlayerRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

private const val LOUNGE_NUMBER = 3

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel() as T
        }

        throw IllegalArgumentException("Unknown modelView class")
    }

}

class HomeViewModel : ViewModel() {

    private val _player: MutableLiveData<Resource<Player>> = MutableLiveData()
    val player: LiveData<Resource<Player>> = _player

    private val _managedPlayers: MutableLiveData<Resource<List<Player>>> = MutableLiveData()
    val managedPlayers: LiveData<Resource<List<Player>>> = _managedPlayers

    private val _lounges: MutableLiveData<Resource<List<Lounge>>> = MutableLiveData()
    val lounges: LiveData<Resource<List<Lounge>>> = _lounges

    private val loungeRepo = LoungeRepository
    private val playerRepo = PlayerRepository


    fun processArgs(args: HomeFragmentArgs) = playerRepo
            .getPlayer(args.playerId)
            .onEach {  _player.value = it }
            .launchIn(viewModelScope)


    fun setHomeEvent(homeEvent: HomeEvent) {

        when (homeEvent) {

            is HomeEvent.JoinLoungeEvent -> {

                val lounge = homeEvent.lounge
                val player = player.value!!
                val managedPlayers = managedPlayers.value!!

                viewModelScope.launch {
                    loungeRepo.joinLounge().onEach { resource ->

                    }
                }

            }

            is HomeEvent.LeaveLoungeEvent -> {

                val lounge = homeEvent.lounge


                viewModelScope.launch {
                    loungeRepo.leaveLounge().onEach { resource ->


                    }

                }
            }

            is HomeEvent.StartGameEvent -> {

            }

        }
    }

}

sealed class HomeEvent {

    class JoinLoungeEvent(val lounge: Lounge): HomeEvent()

    class LeaveLoungeEvent(val lounge: Lounge): HomeEvent()

    object StartGameEvent: HomeEvent()

}