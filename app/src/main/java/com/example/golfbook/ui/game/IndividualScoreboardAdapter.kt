package com.example.golfbook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.data.model.Hole
import com.example.golfbook.extensions.ViewExtensions.textChanges
import com.google.android.material.textfield.TextInputEditText

class IndividualRecyclerViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val holeNumber: TextView = itemView.findViewById(R.id.scoreboardHoleNumber)
    val parNumber: TextView = itemView.findViewById(R.id.scoreboardHolePar)
    val scoreEditText: TextInputEditText = itemView.findViewById(R.id.editTextScore)

}

class IndividualScoreboardAdapter(
        private val onTextChange: (String) -> Unit
) : RecyclerView.Adapter<IndividualRecyclerViewViewHolder>() {

    private var playerScores: List<Pair<Int, Int?>>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndividualRecyclerViewViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_individual_scoreboard, parent, false)

        return IndividualRecyclerViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndividualRecyclerViewViewHolder, position: Int) {

        playerScores?.let {

            val columnData = it[position]

            holder.holeNumber.text = (position+1).toString()
            holder.parNumber.text = columnData.first.toString()

            if (columnData.second != null){
                holder.scoreEditText.setText(columnData.second!!.toString())
            }

            holder.scoreEditText.addTextChangedListener { text ->
                text?.let {
                    onTextChange(text.toString())
                }
            }


        }


    }

    override fun getItemCount(): Int = playerScores?.size ?: 0

    fun updateData(playerScores: List<Pair<Int, Int?>>) {
        this.playerScores = playerScores
        notifyDataSetChanged()
    }



}