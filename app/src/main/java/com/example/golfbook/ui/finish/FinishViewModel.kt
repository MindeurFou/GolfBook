package com.example.golfbook.ui.finish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.golfbook.model.Player
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val args: HomeFragmentArgs) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown modelView class")
    }

}

class HomeViewModel(args: HomeFragmentArgs) : ViewModel() {

    private val _viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewState: LiveData<HomeViewState> = _viewState

    init {

        _viewState.value = HomeViewState(
            player = Player(args.name, args.resourceDrawable)
        )
    }



}