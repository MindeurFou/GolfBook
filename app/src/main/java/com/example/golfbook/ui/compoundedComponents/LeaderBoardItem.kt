package com.example.golfbook.ui.compoundedComponents

import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.golfbook.R
import kotlinx.android.synthetic.main.item_leaderboard.view.*

class LeaderBoardItem(context: Context): ConstraintLayout(context) {

    val name: TextView
    val par: TextView

    init {
        inflate(context, R.layout.item_leaderboard, this)

        name = leaderBoardName
        par = leaderBoardPar
    }


}