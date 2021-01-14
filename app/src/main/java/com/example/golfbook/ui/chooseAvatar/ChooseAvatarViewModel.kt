package com.example.golfbook.ui.chooseAvatar

import androidx.lifecycle.*
import com.example.golfbook.R
import com.example.golfbook.data.model.Player
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ChooseAvatarViewModelFactory(private val args: ChooseAvatarFragmentArgs) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ChooseAvatarViewModel::class.java)){
            return ChooseAvatarViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")

    }

}

class ChooseAvatarViewModel(args: ChooseAvatarFragmentArgs) : ViewModel() {

    private val _viewState: MutableLiveData<Player> = MutableLiveData()
    val viewState: LiveData<Player> = _viewState

    init {

            val player = args.player ?: Player(null, R.drawable.man1, args.isMainPlayer)

            _viewState.value = player

    }

    fun updateAvatar(resourceId: Int) {
        _viewState.value!!.avatarResourceId = resourceId
    }




}