package com.example.golfbook.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.model.Hole

class IndividualRecyclerViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}

class IndividualScoreboardAdapter(
    private val playerScores: Map<Hole, Int>
) : RecyclerView.Adapter<IndividualRecyclerViewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndividualRecyclerViewViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: IndividualRecyclerViewViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}