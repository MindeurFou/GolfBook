package com.example.golfbook.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.golfbook.model.Player

class ChooseAvatarViewModel(receivedPlayer: Player?) : ViewModel() {

    val player: LiveData<Player> = liveData {
        val data = repo.loadUser()
        emit(data)
    }

    fun update() {

        player.value?.name = "c"
    }
}