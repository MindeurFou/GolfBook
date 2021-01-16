package com.example.golfbook.data.repository

import com.example.golfbook.data.model.Course
import com.example.golfbook.data.remote.RemoteCourseDataSource
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CourseRepository {

    private val remoteCourseDataSource = RemoteCourseDataSource

    suspend fun getCourse(courseId: String) : Flow<Resource<Course?>> = flow {

        emit(Resource.Loading)

        try {
            val course = remoteCourseDataSource.getCourse(courseId)
            emit(Resource.Success(course))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    suspend fun putCourse(course: Course) : Flow<Resource<String>> = flow {

        emit(Resource.Loading)

        try {
            val id = remoteCourseDataSource.putCourse(course)
            emit(Resource.Success(id))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}