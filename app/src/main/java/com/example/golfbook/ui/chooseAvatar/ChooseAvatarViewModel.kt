package com.example.golfbook.ui.chooseAvatar

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
class ChooseAvatarViewModelFactory(val player: Player?): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ChooseAvatarViewModel::class.java)){
            return ChooseAvatarViewModel(player) as T
        }

        throw IllegalArgumentException("Unknown modelView class")
    }

}

class ChooseAvatarViewModel(
        private val currentPlayer: Player?
) : ViewModel() {

    private val _player: MutableLiveData<Player> = MutableLiveData()
    val player: LiveData<Player> = _player

    private val _savePlayerState: MutableLiveData<Resource<String>> = MutableLiveData()
    val savePlayerState: LiveData<Resource<String>> = _savePlayerState

    private lateinit var action: chooseAvatarAction

    private val repository = PlayerRepository


    // créé le livedata de player au createView du fragment
    fun processArgs(args: ChooseAvatarFragmentArgs) {

        action = args.chooseAvatarAction

        when (action) {

            chooseAvatarAction.CREATE_MAIN_PLAYER -> {
                _player.value = Player(
                        playerId = null,
                        name = null,
                        drawableResourceId = R.drawable.man1,
                        managerId = null
                )
            }

            chooseAvatarAction.CREATE_MANAGED_PLAYER -> {

                _player.value = Player(
                        playerId = null,
                        name = null,
                        drawableResourceId = R.drawable.man1,
                        managerId = currentPlayer?.playerId ?: throw Exception("managedPlayer without manager")
                )

            }

            chooseAvatarAction.UPDATE_MAIN_PLAYER -> {

                currentPlayer?.let {

                    _player.value = it

                } ?: throw Exception("No current player")

            }
        }

    }


    fun setChooseAvatarEvent(event: ChooseAvatarEvent) {

        when (event) {

            is ChooseAvatarEvent.ChangeDrawableResourceIdEvent -> _player.value = player.value?.apply {
                drawableResourceId = event.drawableResourceId
            }

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
                        when (action) {

                            chooseAvatarAction.CREATE_MAIN_PLAYER, chooseAvatarAction.CREATE_MANAGED_PLAYER -> {
                                repository.putPlayer(player)
                                        .onEach { resource ->
                                            _savePlayerState.value = resource
                                        }.launchIn(viewModelScope)
                            }

                            chooseAvatarAction.UPDATE_MAIN_PLAYER -> {

                                repository.updatePlayer(player)
                                        .onEach { resource ->
                                            _savePlayerState.value = resource
                                        }.launchIn(viewModelScope)
                            }

                        }
                    }

                }

            }


        }
    }

    companion object {

        enum class chooseAvatarAction {CREATE_MAIN_PLAYER, UPDATE_MAIN_PLAYER, CREATE_MANAGED_PLAYER}
    }

}

sealed class ChooseAvatarEvent {

    class ChangeDrawableResourceIdEvent(val drawableResourceId: Int) : ChooseAvatarEvent()

    class ChangeNameEvent(val name: String) : ChooseAvatarEvent()

    object SavePlayerEvent : ChooseAvatarEvent()
}