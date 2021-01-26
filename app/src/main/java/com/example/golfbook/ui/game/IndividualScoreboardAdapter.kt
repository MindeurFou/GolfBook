package com.example.golfbook.ui.adapters

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.data.model.Hole
import com.example.golfbook.extensions.ViewExtensions.textChanges
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class IndividualRecyclerViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val holeNumber: TextView = itemView.findViewById(R.id.scoreboardHoleNumber)
    val parNumber: TextView = itemView.findViewById(R.id.scoreboardHolePar)
    val scoreEditText: TextView = itemView.findViewById(R.id.score)

}

class IndividualScoreboardAdapter(
        private val context: Context,
        private val onTextChange: (index: Int, score: Int) -> Unit
) : RecyclerView.Adapter<IndividualRecyclerViewViewHolder>() {

    private var playerScores: List<Pair<Int, Int?>>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndividualRecyclerViewViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_individual_scoreboard, parent, false)

        return IndividualRecyclerViewViewHolder(view)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onBindViewHolder(holder: IndividualRecyclerViewViewHolder, position: Int) {

        playerScores?.let {

            val columnData = it[position]

            holder.holeNumber.text = (position+1).toString()
            holder.parNumber.text = columnData.first.toString()

            if (columnData.second != null){
                holder.scoreEditText.setText(columnData.second!!.toString())
            }

            holder.scoreEditText.setOnClickListener {
                val dialog = NumberPickerDialog(context,1,10
                ) { int ->
                    onTextChange(position, int)
                }

                dialog.setTitle(context.resources.getString(R.string.hole_input_title))
                dialog.show()


            }


        }


    }

    override fun getItemCount(): Int = playerScores?.size ?: 0

    fun updateData(playerScores: List<Pair<Int, Int?>>) {
        this.playerScores = playerScores
        notifyDataSetChanged()
    }



}