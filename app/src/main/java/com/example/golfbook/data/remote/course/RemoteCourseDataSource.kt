package com.example.golfbook.data.remote.course

import com.example.golfbook.utils.Resource
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

        val map: Map<String, String> = mapOf("name" to course.name)
        courseNameListCollectionRef.add(map).await()

        return id
    }


    suspend fun getCourse(courseId: String) : FirestoreCourseEntity?  {
        val document = courseCollectionRef.document(courseId).get().await()
        return document.toObject()
    }

    suspend fun getCourseByName(name: String) : FirestoreCourseEntity?  {

        val querySnapshot = courseCollectionRef.whereEqualTo("name", name).get().await()

        if (querySnapshot.documents.size == 1) {

            val document = querySnapshot.documents[0]

            return document.toObject()
        }

        return null
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

    fun subscribeToCourseName(setLiveData: (Resource<List<String>>) -> Unit) = courseNameListCollectionRef.addSnapshotListener { querySnapshot, firestoreException ->

        firestoreException?.let {
            setLiveData(Resource.Failure(it))
        }

        querySnapshot?.let { querySnapshot ->

            val listCoursesName : MutableList<String> = mutableListOf()

            for (document in querySnapshot.documents){

                val name = document.getString("name")

                name?.let {
                    listCoursesName.add(name)
                }
            }

            setLiveData(Resource.Success(listCoursesName))
        }
    }




}