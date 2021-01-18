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

    private val _numberOfHole: MutableLiveData<Int> = MutableLiveData(18)
    val numberOfHole: LiveData<Int> = _numberOfHole

    private var _holes: MutableList<Hole> = MutableList(18) { index ->
        Hole(index + 1, null)
    }

    var holes: List<Hole> = _holes

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

                _holes[event.holeNumber-1] = Hole(event.holeNumber, event.par)

            }

            is CreateCourseEvent.SetNumberOfHoleEvent -> {

                //cast la list de 18 vers 9
                if (event.numberOfHoles == 9) {

                    val list = _holes.filterIndexed { index, hole -> index < 9 }
                    holes = list
                    _holes = list.toMutableList()


                    _numberOfHole.value = 9
                }

                // rallonge la liste
                else if (event.numberOfHoles == 18) {

                    for (i in 10..18){

                        val hole = Hole(i,null)
                        _holes.add(i-1, hole)
                    }

                    holes = _holes
                    _numberOfHole.value = event.numberOfHoles


                }

            }

            is CreateCourseEvent.SaveCourseEvent -> {

                if (courseIsValid()) {

                    var par = 0

                    for (hole in _holes)
                        par += hole.par!!

                    val course = Course(
                            name = name.value!!,
                            numberOfHoles = numberOfHole.value!!,
                            holes = _holes,
                            par = par,
                            gamesPlayed = 0
                    )

                    viewModelScope.launch {
                        repository.putCourse(course).onEach { resource ->
                            _saveCourseSate.value = resource
                        }.launchIn(viewModelScope)
                    }

                } else {
                    _saveCourseSate.value = Resource.Failure(Exception("Veuillez remplir correctement les champs"))
                }


            }
        }

    }

    private fun courseIsValid() : Boolean {

        if (name.value == null)
            return false

        numberOfHole.value?.let {
            if(numberOfHole.value != 9 && numberOfHole.value != 18 )
                return false
        } ?: return false

        for (hole in _holes){
            if (hole.par == null)
                return false
        }

        return true
    }
}

sealed class CreateCourseEvent {

    class SetNumberOfHoleEvent(val numberOfHoles: Int) : CreateCourseEvent()

    class SetHoleEvent(val holeNumber: Int, val par: Int) : CreateCourseEvent()

    class SetNameEvent(val name: String) : CreateCourseEvent()

    object SaveCourseEvent : CreateCourseEvent()
}