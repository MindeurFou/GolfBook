package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Course
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.remote.course.RemoteCourseDataSource
import com.example.golfbook.data.remote.course.RemoteCourseMapper
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CourseRepository {

    private val remoteCourseDataSource = RemoteCourseDataSource
    private val remoteCourseMapper = RemoteCourseMapper

    suspend fun getCourse(courseId: String) : Flow<Resource<Course>> = flow {

        emit(Resource.Loading)

        try {
            val firestoreCourseEntity = remoteCourseDataSource.getCourse(courseId)

            firestoreCourseEntity?.let {

                val course = remoteCourseMapper.mapFromEntity(firestoreCourseEntity)
                emit(Resource.Success(course))

            }  ?: emit(Resource.Failure(Exception("Le parcours n'as pas été trouvé")))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    suspend fun getCourseByName(name: String) : Flow<Resource<Course>> = flow {

        emit(Resource.Loading)

        try {
            val firestoreCourseEntity = remoteCourseDataSource.getCourseByName(name)

            firestoreCourseEntity?.let {

                val course = remoteCourseMapper.mapFromEntity(firestoreCourseEntity)
                emit(Resource.Success(course))

            }  ?: emit(Resource.Failure(Exception("Le parcours n'as pas été trouvé")))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    suspend fun getAllCourses() : Flow<Resource<List<Course>>> = flow {

        emit(Resource.Loading)

        try {

            val listFirestoreEntity = remoteCourseDataSource.getAllCourses()
            val list = remoteCourseMapper.mapFromEntityList(listFirestoreEntity)

            emit(Resource.Success(list))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    suspend fun getAllCoursesName() : Flow<Resource<List<String>>> = flow {

        emit(Resource.Loading)

        try {

            val list = remoteCourseDataSource.getAllCoursesName()

            emit(Resource.Success(list))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun subscribeToCourseName(setLiveData: (Resource<List<String>>) -> Unit) = RemoteCourseDataSource.subscribeToCourseName(setLiveData)

    suspend fun putCourse(course: Course) : Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {
            val firestoreCourseEntity = remoteCourseMapper.mapToEntity(course)
            val id = remoteCourseDataSource.putCourse(firestoreCourseEntity)
            emit(Resource.Success(id))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }
}