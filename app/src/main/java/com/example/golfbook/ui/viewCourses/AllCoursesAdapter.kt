package com.example.golfbook.ui.viewCourses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R

class AllCoursesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val courseName: TextView = itemView.findViewById(R.id.courseName)
    //val numberOfGames: TextView = itemView.findViewById(R.id.numberGamePlayed)

}


class AllCoursesAdapter(
    private val listNameCourse: List<String>,
    private val navigateToCourseDetails: (String) -> Unit
) : RecyclerView.Adapter<AllCoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCoursesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return AllCoursesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllCoursesViewHolder, position: Int) {

        val name = listNameCourse[position]

        holder.courseName.text = name
        holder.itemView.setOnClickListener {
            navigateToCourseDetails(name)
        }

    }

    override fun getItemCount(): Int = listNameCourse.size

}