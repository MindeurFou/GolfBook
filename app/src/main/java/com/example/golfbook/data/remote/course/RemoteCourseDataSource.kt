package com.example.golfbook.data.remote.course

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object RemoteCourseDataSource {

    private val firestore = Firebase.firestore
    private val courseCollectionRef = firestore.collection("course")
    private val courseNameListCollectionRef = firestore.collection("coursesName")

    suspend fun putCourse(course: FirestoreCourseEntity) : String {

        val id = courseCollectionRef.add(course).await().id

        courseNameListCollectionRef.add(course.name).await()

        return id
    }


    suspend fun getCourse(courseId: String) : FirestoreCourseEntity?  {
        val document = courseCollectionRef.document(courseId).get().await()
        return document.toObject()
    }

    suspend fun getAllCourses() : List<Pair<String,FirestoreCourseEntity>> {

        val querySnapshot = courseCollectionRef.get().await()

        val list: MutableList<Pair<String, FirestoreCourseEntity>> = mutableListOf()


        for (document in querySnapshot.documents){

            document.toObject<FirestoreCourseEntity>()?.let {

                val pair = Pair(document.id, it)
                list.add(pair)
            }
        }

        return list
    }

    suspend fun getAllCoursesName() : List<String> {

        val list: MutableList<String> = mutableListOf()

       val querySnapshot =  courseNameListCollectionRef.get().await()

        for (document in querySnapshot.documents) {

            list.add(document["name"] as String)
        }

        return list
    }


}