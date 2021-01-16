package com.example.golfbook.data.remote

import com.example.golfbook.data.model.Course
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.data.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object RemoteCourseDataSource {

    private val firestore = Firebase.firestore

    private val courseCollectionRef = firestore.collection("course")


    suspend fun putCourse(course: Course) : String = courseCollectionRef.add(course).await().id

    suspend fun getCourse(courseId: String) : Course?  {
        val document = courseCollectionRef.document(courseId).get().await()
        return document.toObject()
    }


}