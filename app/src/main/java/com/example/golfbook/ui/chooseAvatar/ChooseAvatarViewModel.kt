package com.example.golfbook.ui.chooseAvatar

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.golfbook.R
import com.example.golfbook.data.model.Player
import com.example.golfbook.data.repository.PlayerRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ChooseAvatarViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ChooseAvatarViewModel::class.java)){
            return ChooseAvatarViewModel() as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")

    }

}

class ChooseAvatarViewModel : ViewModel() {

    private val _player: MutableLiveData<Player> = MutableLiveData()
    val player: LiveData<Player> = _player

    private val _savePlayerState: MutableLiveData<Resource<String>> = MutableLiveData()
    val savePlayerState: LiveData<Resource<String>> = _savePlayerState

    private val repository = PlayerRepository


    // créé le livedata de player au createView du fragment
    fun processArgs(args: ChooseAvatarFragmentArgs) {

        val player = if (args.name == null || args.drawableResourceId == -1) { // si le joueur recu est vide
            Player(
                    playerId = null,
                    name = null,
                    drawableResourceId = R.drawable.man1,
                    managerId = null
            )
        } else {

            val managerId = if (args.managerId == "null") {
                null
            } else {
                args.managerId
            }

            val playerId = if (args.playerId == "null")
                null
            else
                args.playerId


            Player(
                    playerId = playerId,
                    name = args.name,
                    drawableResourceId = args.drawableResourceId,
                    managerId = managerId
            )

        }

        _player.value = player
    }


    fun setChooseAvatarEvent(event: ChooseAvatarEvent) {

        when (event) {

            is ChooseAvatarEvent.ChangeDrawableResourceIdEvent -> _player.value!!.drawableResourceId = event.drawableResourceId

            is ChooseAvatarEvent.ChangeNameEvent -> {

                if (event.name.isNotBlank()) {
                    _player.value = _player.value?.apply {
                        name = event.name
                    }
                }
            }

            is ChooseAvatarEvent.SavePlayerEvent -> {

                val player = player.value!!

                if (player.name == null || player.drawableResourceId == -1) {
                    _savePlayerState.value = Resource.Failure(Exception("Veuillez remplir correctement les champs"))
                } else{

                    viewModelScope.launch {
                        repository.putPlayer(player)
                            .onEach { resource ->
                            _savePlayerState.value = resource
                        }.launchIn(viewModelScope)
                    }

                }

            }


        }
    }

}

sealed class ChooseAvatarEvent {

    class ChangeDrawableResourceIdEvent(val drawableResourceId: Int) : ChooseAvatarEvent()

    class ChangeNameEvent(val name: String) : ChooseAvatarEvent()

    object SavePlayerEvent : ChooseAvatarEvent()
}