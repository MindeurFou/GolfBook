package com.example.golfbook.ui.compoundedComponents

import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.golfbook.R

class LeaderBoardItem(context: Context): ConstraintLayout(context) {

    val name: TextView
    val par: TextView

    init {
        val view = inflate(context, R.layout.item_leaderboard, this)

        name = view.findViewById(R.id.leaderBoardName)
        par = view.findViewById(R.id.leaderBoardPar)
    }


}