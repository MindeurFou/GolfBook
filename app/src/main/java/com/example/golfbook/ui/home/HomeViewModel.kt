package com.example.golfbook.ui.home


import androidx.lifecycle.*
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.repository.HomeRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

private const val LOUNGE_NUMBER = 3

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val args: HomeFragmentArgs) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown modelView class")
    }

}

class HomeViewModel (args: HomeFragmentArgs)  : ViewModel() {

    private val _viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewState: LiveData<HomeViewState> = _viewState

    private val _dataState: MutableLiveData<Resource<Any>> = MutableLiveData()
    val dataState: LiveData<Resource<Any>> = _dataState

    private val repository = HomeRepository

    init {


        // quand on passe par l'init c'est que c'est la première fois qu'on crée le viewmodel : on init 3 salons
        val lounges: MutableList<Lounge> = mutableListOf()

        for (iterator in 1..LOUNGE_NUMBER) {

            lounges.add(Lounge(
                    loungeNumber = iterator
            ))

        }

        if (!args.player.isRealUser)
            throw Exception("you didn't understand viewmodel")


        _viewState.value = HomeViewState(
                player = args.player,   // le joueur passé est forcément le mainPlayer ici
                lounges = lounges,
                managedPlayers = null   // pas encore de managedPlayer
        )

        // TODO gérer la reception de joueurs "managed"
    }


    fun setHomeEvent(homeEvent: HomeEvent) {

        viewModelScope.launch {
            when (homeEvent) {

                is HomeEvent.SavePlayerEvent -> {
                    repository
                            .putPlayer(viewState.value!!.player)
                            .onEach { resource ->
                                _dataState.value = resource
                            }
                            .launchIn(viewModelScope)
                }

                is HomeEvent.JoinLoungeEvent -> {

                    val lounge = homeEvent.lounge
                    val player = viewState.value!!.player
                    val managedPlayers = viewState.value!!.managedPlayers

                    repository
                            .joinLounge(player, managedPlayers, lounge)
                            .onEach { resource ->

                                _dataState.value = resource

                                if (resource is Resource.Success)
                                    _viewState.value!!.lounges = listOf(resource.data) // TODO faire une vraie liste avec tout les salons mis à jour
                            }
                            .launchIn(viewModelScope)
                }

                is HomeEvent.LeaveLoungeEvent -> {

                    val lounge = homeEvent.lounge
                    val player = viewState.value!!.player
                    val managedPlayers = viewState.value!!.managedPlayers

                    repository
                            .leaveLounge(player, managedPlayers, lounge)
                            .onEach {  resource ->

                                _dataState.value = resource


                                if (resource is Resource.Success)
                                    _viewState.value!!.lounges = listOf(resource.data)


                            }
                            .launchIn(viewModelScope)
                }

                is HomeEvent.StartGameEvent -> {

                }
            }
        }
    }

}

sealed class HomeEvent {

    object SavePlayerEvent: HomeEvent()

    class JoinLoungeEvent(val lounge: Lounge): HomeEvent()

    class LeaveLoungeEvent(val lounge: Lounge): HomeEvent()

    object StartGameEvent: HomeEvent()

}