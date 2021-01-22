package com.example.golfbook.ui.loungeDetails

import androidx.lifecycle.*
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.repository.CourseRepository
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
        private val mainPlayer: Player
) : ViewModel() {

    private val loungeRepo = LoungeRepository
    private val courseRepo = CourseRepository

    private val _listCoursesName: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val listCoursesName: LiveData<Resource<List<String>>> = _listCoursesName

    private val _lounge: MutableLiveData<Resource<Lounge>> = MutableLiveData()
    val lounge: LiveData<Resource<Lounge>> = _lounge

    private val _leaveLoungeState: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val leaveLoungeState: LiveData<Resource<Unit>> = _leaveLoungeState

    private val courseNameRegistration = courseRepo.subscribeToCourseName { names ->
        _listCoursesName.value = names
    }

    private val loungeListenerRegistration = loungeRepo.subscribeToALounge(args.loungeId) { loungeResource ->
        _lounge.value = loungeResource
    }


    fun leaveLounge() {

        if ( lounge.value is Resource.Success) {

            val lounge = (lounge.value as Resource.Success<Lounge>).data

            loungeRepo.leaveLounge(lounge, mainPlayer).onEach {
                _leaveLoungeState.value = it
            }.launchIn(viewModelScope)
        }

    }

    override fun onCleared() {
        super.onCleared()
        courseNameRegistration.remove()
        loungeListenerRegistration.remove()
    }



}