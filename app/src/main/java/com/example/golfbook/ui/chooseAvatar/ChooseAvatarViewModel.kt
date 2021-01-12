package com.example.golfbook.ui.chooseAvatar

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.*
import com.example.golfbook.R
import com.example.golfbook.model.Player
import java.lang.IllegalArgumentException
import kotlin.random.Random

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

        if ( args.isMainPlayer ) {

            val name = if( args.name == "null"){
                null
            } else {
                args.name
            }

            val resourceId = if (args.drawableResource == -1) {
                R.drawable.man1
            } else {
                args.drawableResource
            }

            _viewState.value = Player(
                name = name,
                avatarResourceId = resourceId
            )

        }


    }

    fun updateAvatar(resourceId: Int) {
        _viewState.value!!.avatarResourceId = resourceId
    }




}