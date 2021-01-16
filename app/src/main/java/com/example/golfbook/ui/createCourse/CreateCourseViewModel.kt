package com.example.golfbook.ui.createCourse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfbook.data.model.Course
import com.example.golfbook.data.model.Hole
import com.example.golfbook.data.repository.CourseRepository
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CreateCourseViewModel : ViewModel() {

    private val _numberOfHole: MutableLiveData<Int?> = MutableLiveData()
    val numberOfHole: LiveData<Int?> = _numberOfHole

    private val _holes: MutableLiveData<List<Hole>> = MutableLiveData()
    val holes: LiveData<List<Hole>> = _holes

    private val _par: MutableLiveData<Int> = MutableLiveData()
    val par: LiveData<Int> = _par

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> = _name

    private val _saveCourseSate: MutableLiveData<Resource<String>> = MutableLiveData()
    val saveCourseState: LiveData<Resource<String>> = _saveCourseSate

    private val repository = CourseRepository

    fun setCreateCourseEvent(event: CreateCourseEvent) {

        when(event) {

            is CreateCourseEvent.SetNameEvent -> {

                if (event.name.isNotBlank())
                    _name.value = event.name

            }

            is CreateCourseEvent.SetHoleEvent -> {

            }

            is CreateCourseEvent.SetNumberOfHoleEvent -> {

                if (event.numberOfHoles == 9 || event.numberOfHoles == 18)
                    _numberOfHole.value = event.numberOfHoles
            }

            is CreateCourseEvent.SaveCourseEvent -> {

                val numberOfHoles = numberOfHole.value

                // TODO faire les autres tests

                numberOfHoles?.let {

                    val course = Course() // TODO

                    viewModelScope.launch {
                        repository.putCourse(course).onEach { resource ->
                            _saveCourseSate.value = resource
                        }.launchIn(viewModelScope)
                    }


                }
            }
        }

    }

}

sealed class CreateCourseEvent {

    class SetNumberOfHoleEvent(val numberOfHoles: Int) : CreateCourseEvent()

    class SetHoleEvent(val holeNumber: Int, val par: Int) : CreateCourseEvent()

    class SetNameEvent(val name: String) : CreateCourseEvent()

    object SaveCourseEvent : CreateCourseEvent()
}