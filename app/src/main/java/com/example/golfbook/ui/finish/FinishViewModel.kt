package com.example.golfbook.ui.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class FinishViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FinishViewModel::class.java)){
            return FinishViewModel() as T
        }

        throw IllegalArgumentException("Unknown modelView class")
    }

}

class FinishViewModel() : ViewModel() {



}