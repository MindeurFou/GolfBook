package com.example.golfbook.ui.home


import androidx.compose.runtime.onActive
import androidx.lifecycle.*
import com.example.golfbook.data.model.Course
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.remote.lounge.FirestoreLoungeEntity
import com.example.golfbook.data.remote.lounge.RemoteLoungeDataSource
import com.example.golfbook.data.remote.player.FirestorePlayerEntity
import com.example.golfbook.data.repository.CourseRepository
import com.example.golfbook.data.repository.LoungeRepository
import com.example.golfbook.data.repository.PlayerRepository
import com.example.golfbook.utils.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
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

    private val _managedPlayers: MutableLiveData<List<Player>> = MutableLiveData()
    val managedPlayers: LiveData<List<Player>> = _managedPlayers

    private val _lounges: MutableLiveData<Resource<List<Lounge>>> = MutableLiveData()
    val lounges: LiveData<Resource<List<Lounge>>> = _lounges

    private val _listCoursesName: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val listCoursesName: LiveData<Resource<List<String>>> = _listCoursesName

    private val loungeRepo = LoungeRepository
    private val playerRepo = PlayerRepository
    private val courseRepo = CourseRepository

    private val loungeListenerRegistration = loungeRepo.subscribe { lounges ->
        _lounges.value = lounges
    }

    private val courseNameRegistration = courseRepo.subscribeToCourseName { names ->
        _listCoursesName.value = names
    }


    override fun onCleared() {
        super.onCleared()
        loungeListenerRegistration.remove() // arrête d'écouter les modifs de lounge
        courseNameRegistration.remove()
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