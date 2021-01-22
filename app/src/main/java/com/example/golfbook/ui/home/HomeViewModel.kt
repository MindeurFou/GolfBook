package com.example.golfbook.ui.home


import androidx.lifecycle.*
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.repository.LoungeRepository
import com.example.golfbook.data.repository.PlayerRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    var mainPlayer: Player? = null

    private val _managedPlayers: MutableLiveData<List<Player>> = MutableLiveData()
    val managedPlayers: LiveData<List<Player>> = _managedPlayers

    private val _lounges: MutableLiveData<Resource<List<Lounge>>> = MutableLiveData()
    val lounges: LiveData<Resource<List<Lounge>>> = _lounges

    private val _joinLoungeState: MutableLiveData<Resource<String>> = MutableLiveData()
    val joinLoungeState: LiveData<Resource<String>> = _joinLoungeState

    private val loungeRepo = LoungeRepository
    private val playerRepo = PlayerRepository

    private val loungeListenerRegistration = loungeRepo.subscribeToAllLounges { lounges ->
        _lounges.value = lounges
    }

    override fun onCleared() {
        super.onCleared()
        loungeListenerRegistration.remove() // arrête d'écouter les modifs de lounge
    }

    fun retrievePlayerAndManagedPlayers(playerId: String) {

        playerRepo.getPlayer(playerId).onEach { resource ->

            if (resource is Resource.Success) {

                if (resource.data.managerId != null) {
                    val listManagedPlayers = _managedPlayers.value?.toMutableList() ?: mutableListOf()

                    listManagedPlayers.add(resource.data)

                    _managedPlayers.value = listManagedPlayers
                }
                else {
                    _player.value = resource
                    mainPlayer = resource.data
                }
            } else {
                _player.value = resource
            }
        }.launchIn(viewModelScope)

        playerRepo.getManagedPlayersLinkedTo(playerId).onEach {
            // TODO
        }.launchIn(viewModelScope)

    }

    fun setHomeEvent(homeEvent: HomeEvent) {

        when (homeEvent) {

            is HomeEvent.JoinLoungeEvent -> {

                val lounge = homeEvent.lounge
                val player = mainPlayer!!
                val managedPlayers = managedPlayers.value

                loungeRepo.joinLounge(lounge, player).onEach { resource ->
                    _joinLoungeState.value = resource
                }.launchIn(viewModelScope)



            }

        }

    }

}

sealed class HomeEvent {

    class JoinLoungeEvent(val lounge: Lounge): HomeEvent()

}