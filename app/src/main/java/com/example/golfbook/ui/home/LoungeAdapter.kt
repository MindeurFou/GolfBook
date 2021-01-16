package com.example.golfbook.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.data.model.Lounge
import com.google.android.material.textfield.TextInputEditText


class LoungeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val loungeName: TextView = itemView.findViewById(R.id.loungeTitle)
    val courseEditText: TextInputEditText = itemView.findViewById(R.id.editTextCourse)
    val playersContainer: LinearLayout = itemView.findViewById(R.id.playersContainer)
    val btnStart: Button = itemView.findViewById(R.id.btnStartGame)
    val btnJoin: ImageButton = itemView.findViewById(R.id.btnJoin)
    val btnLeave: ImageButton = itemView.findViewById(R.id.btnLeave)

    fun bind(lounge: Lounge) {

        loungeName.text = itemView.context.getString(R.string.loungeName, lounge.loungeNumber)

        // TODO dropdown editText

        lounge.players?.let { players ->

            for (player in players) {

                val playerName = TextView(playersContainer.context).apply {
                    text = player.name
                }

                playersContainer.addView(playerName)
            }

        }


        btnStart.setOnClickListener { view ->

            if (lounge.course == null) {
                Toast.makeText(view.context, R.string.emptyCourse, Toast.LENGTH_LONG).show()
            } else {

                lounge.players?.let { players ->

                    if (players.size == 1)
                        Toast.makeText(view.context, R.string.lonelyPlayer, Toast.LENGTH_LONG).show()
                    else {
                        val action = HomeFragmentDirections.actionHomeFragmentToGameViewPagerFragment("bite")
                        view.findNavController().navigate(action)
                    }

                } ?: Toast.makeText(view.context, R.string.emptyLounge, Toast.LENGTH_LONG).show()
            }

        }


    }

}

class LoungeAdapter(
       private val joinLounge: (lounge: Lounge) -> Unit,
       private val leaveLounge: (lounge: Lounge) -> Unit,
) : RecyclerView.Adapter<LoungeViewHolder>() {

    private var lounges: List<Lounge> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoungeViewHolder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_lounge, parent, false)

        return LoungeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoungeViewHolder, position: Int) {

        val lounge = lounges[position]
        holder.bind(lounge)

        holder.btnJoin.setOnClickListener { joinLounge(lounge) }
        holder.btnLeave.setOnClickListener { leaveLounge(lounge) }

    }


    override fun getItemCount(): Int = lounges.size

    fun updateLounges(lounges: List<Lounge>) {
        this.lounges = lounges
        notifyDataSetChanged()
    }

}