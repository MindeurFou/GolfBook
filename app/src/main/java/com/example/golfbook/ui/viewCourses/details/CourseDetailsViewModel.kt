package com.example.golfbook.ui.viewCourses.details

import androidx.lifecycle.*
import com.example.golfbook.data.model.Course
import com.example.golfbook.data.repository.CourseRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class CourseDetailsViewModelFactory(private val args: CourseDetailsFragmentArgs) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CourseDetailsViewModel::class.java)) {
            return CourseDetailsViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")

    }

}

class CourseDetailsViewModel(
    private val args: CourseDetailsFragmentArgs
) : ViewModel() {

    private val _course: MutableLiveData<Resource<Course>> = MutableLiveData()
    val course: LiveData<Resource<Course>> = _course

    private val courseRepository = CourseRepository

    init {

        viewModelScope.launch {

            courseRepository.getCourseByName(args.courseName).onEach { resource ->
                _course.value = resource
            }.launchIn(viewModelScope)
        }

    }

}