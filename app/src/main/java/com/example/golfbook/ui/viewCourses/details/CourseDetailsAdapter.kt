package com.example.golfbook.ui.viewCourses.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.data.model.Hole

class CourseDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val holeNumber: TextView = itemView.findViewById(R.id.holeNumberTitle)
    val holeValue: TextView = itemView.findViewById(R.id.holeValue)
}


class CourseDetailsAdapter(
    private val context: Context,
    private val listHoles: List<Hole>,
) : RecyclerView.Adapter<CourseDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course_details_hole, parent, false)
        return CourseDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseDetailsViewHolder, position: Int) {

        val hole = listHoles[position]

        holder.holeNumber.text = context.getString(R.string.holeNumber, hole.holeNumber)

        holder.holeValue.text = hole.par.toString()

    }

    override fun getItemCount(): Int = listHoles.size

}