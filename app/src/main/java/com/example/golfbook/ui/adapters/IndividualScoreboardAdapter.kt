package com.example.golfbook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.data.model.Hole
import com.google.android.material.textfield.TextInputEditText

class IndividualRecyclerViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val holeNumber: TextView = itemView.findViewById(R.id.scoreboardHoleNumber)
    val parNumber: TextView = itemView.findViewById(R.id.scoreboardHolePar)
    val scoreEditText: TextInputEditText = itemView.findViewById(R.id.editTextScore)

}

class IndividualScoreboardAdapter(
    private val playerScores: Map<Hole, Int>
) : RecyclerView.Adapter<IndividualRecyclerViewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndividualRecyclerViewViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_individual_scoreboard, parent, false)

        return IndividualRecyclerViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndividualRecyclerViewViewHolder, position: Int) {

        val holePair = getHoleByNumber(position+1)

        holePair?.let {

            holder.holeNumber.text = it.first.holeNumber.toString()
            holder.parNumber.text = it.first.par.toString()

            if (it.second != -1) { //-1 correspond à score pas encore rempli
                holder.scoreEditText.setText(it.second.toString())
            }
        }

    }

    override fun getItemCount(): Int = playerScores.size

    private fun getHoleByNumber(holeNumber: Int) : Pair<Hole, Int>? {

        for (score in playerScores) {

            if (score.key.holeNumber == holeNumber) {
                return Pair(score.key, score.value)
            }

        }

        return null
    }


}