package com.example.golfbook.ui.viewCourses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfbook.data.repository.CourseRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ViewCoursesViewModel : ViewModel() {

    private val _coursesName: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val coursesName: LiveData<Resource<List<String>>> = _coursesName

    private val courseRepository = CourseRepository

    init {

        viewModelScope.launch {
            courseRepository.getAllCoursesName().onEach {
                _coursesName.value = it
            }.launchIn(viewModelScope)
        }
    }
}