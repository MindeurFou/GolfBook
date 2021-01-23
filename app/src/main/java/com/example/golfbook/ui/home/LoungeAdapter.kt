package com.example.golfbook.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.data.model.Lounge
import com.example.golfbook.ui.compoundedComponents.PlayerPreviewItem
import com.example.golfbook.ui.compoundedComponents.PlayerPreviewItemSize
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class LoungeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val loungeName: TextView = itemView.findViewById(R.id.loungeTitle)
    val playersContainer: GridLayout = itemView.findViewById(R.id.playersContainer)
    val btnJoin: MaterialButton = itemView.findViewById(R.id.btnJoinLounge)

    fun bind(lounge: Lounge) {

        loungeName.text = itemView.context.getString(R.string.loungeName, lounge.name)

        lounge.playersInLounge?.let { players ->

            playersContainer.removeAllViews()

            for (player in players) {
                playersContainer.addView(PlayerPreviewItem(itemView.context, player, PlayerPreviewItemSize.SMALL))
            }

            playersContainer.invalidate()
            playersContainer.requestLayout()

        }

    }

}

class LoungeAdapter(
        private val joinLounge: (lounge: Lounge) -> Unit
) : RecyclerView.Adapter<LoungeViewHolder>() {

    private var lounges: List<Lounge> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoungeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lounge, parent, false)
        return LoungeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoungeViewHolder, position: Int) {

        val lounge = lounges[position]
        holder.bind(lounge)

        holder.btnJoin.setOnClickListener { joinLounge(lounge) }
    }


    override fun getItemCount(): Int = lounges.size

    fun updateLounges(lounges: List<Lounge>) {
        this.lounges = lounges
        notifyDataSetChanged()
    }

}