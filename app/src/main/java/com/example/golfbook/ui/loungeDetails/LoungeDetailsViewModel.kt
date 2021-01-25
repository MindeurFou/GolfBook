package com.example.golfbook.ui.loungeDetails

import androidx.lifecycle.*
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.LoungeDetails
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.repository.CourseRepository
import com.example.golfbook.data.repository.GameRepository
import com.example.golfbook.data.repository.LoungeRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class LoungeDetailsViewModelFactory(private val args: LoungeDetailsFragmentArgs, private val player: Player) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoungeDetailsViewModel::class.java)) {
           return LoungeDetailsViewModel(args, player) as T
        }

        throw IllegalArgumentException("Unknown modelView class")
    }

}

class LoungeDetailsViewModel(
        args: LoungeDetailsFragmentArgs,
        private val localPlayer: Player
) : ViewModel() {

    private val loungeRepo = LoungeRepository
    private val courseRepo = CourseRepository
    private val gameRepo = GameRepository

    private val _listCoursesName: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val listCoursesName: LiveData<Resource<List<String>>> = _listCoursesName

    private val _lounge: MutableLiveData<Resource<Lounge>> = MutableLiveData()
    val lounge: LiveData<Resource<Lounge>> = _lounge

    private val _loungeDetails: MutableLiveData<Resource<LoungeDetails>> = MutableLiveData()
    val loungeDetails: LiveData<Resource<LoungeDetails>> = _loungeDetails

    private val _leaveLoungeState: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val leaveLoungeState: LiveData<Resource<Unit>> = _leaveLoungeState

    private val courseNameRegistration = courseRepo.subscribeToCourseName { names ->
        _listCoursesName.value = names
    }

    private val loungeListenerRegistration = loungeRepo.subscribeToALounge(args.loungeId) { loungeResource ->
        _lounge.value = loungeResource
    }

    private val loungeDetailsListenerRegistration = loungeRepo.subscribeToLoungeDetails(args.loungeId) { loungeDetailsResource ->
        _loungeDetails.value = loungeDetailsResource
    }

    var localPlayerIsAdmin = false


    fun leaveLounge() {

        if ( lounge.value is Resource.Success) {

            val lounge = (lounge.value as Resource.Success<Lounge>).data

            loungeRepo.leaveLounge(lounge, localPlayer).onEach {
                _leaveLoungeState.value = it
            }.launchIn(viewModelScope)
        }

    }

    fun updateCourseName(courseName: String) {

        if ( lounge.value is Resource.Success) {

            val lounge = (lounge.value as Resource.Success<Lounge>).data

            loungeRepo.updateCourseName(courseName, lounge).launchIn(viewModelScope)

        }
    }

    fun startGame() {

        if ( lounge.value is Resource.Success) {

            val lounge = (lounge.value as Resource.Success<Lounge>).data

            if (lounge.courseName == null)
                throw Exception("Vous devez choisir un parcours")

            localPlayerIsAdmin = true

            loungeRepo.startGame(lounge, localPlayer.name).launchIn(viewModelScope)

        }
    }

    fun acceptStart() {

        if ( lounge.value is Resource.Success) {

            val lounge = (lounge.value as Resource.Success<Lounge>).data

            loungeRepo.acceptStart(lounge, localPlayer.name).launchIn(viewModelScope)

        }

    }

    fun refuseStart() {

        if ( lounge.value is Resource.Success) {

            val lounge = (lounge.value as Resource.Success<Lounge>).data

            lounge.loungeId?.let {
                loungeRepo.refuseStart(it)
            }


        }

    }

    // appelée juste par le adminPlayer
    fun maybeLaunchGame() {

        if (lounge.value is Resource.Success && loungeDetails.value is Resource.Success) {

            val lounge = (lounge.value!! as Resource.Success).data

            val nbPlayersInLounge = lounge.playersInLounge!!.size
            val nbPlayersReady = (loungeDetails.value!! as Resource.Success).data.playersReady?.size

            nbPlayersReady?.let {

                if (it == nbPlayersInLounge) {
                    gameRepo.createGame(lounge.loungeId!!).onEach { resourceGameId ->

                        if (resourceGameId is Resource.Success)
                            loungeRepo.setGameId(lounge.loungeId, resourceGameId.data)

                    }.launchIn(viewModelScope)
                }
            }
        }


    }

    override fun onCleared() {
        super.onCleared()
        courseNameRegistration.remove()
        loungeListenerRegistration.remove()
        loungeDetailsListenerRegistration.remove()
    }



}