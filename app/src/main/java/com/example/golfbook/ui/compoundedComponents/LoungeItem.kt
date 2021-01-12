package com.example.golfbook.ui.compoundedComponents

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.example.golfbook.R
import com.example.golfbook.ui.home.HomeFragmentDirections
import com.google.android.material.textfield.TextInputEditText

class LoungeItem(context: Context, loungeNumber: Int): ConstraintLayout(context) {

    val courseEditText: TextInputEditText
    val playersContainer: LinearLayout

    init {
        val view = inflate(context, R.layout.item_lounge, this)

        view.findViewById<TextView>(R.id.LoungeTitle).text = context.getString(R.string.loungeName, loungeNumber)

        courseEditText = view.findViewById(R.id.editTextCourse)
        playersContainer = view.findViewById(R.id.playersContainer)

        view.findViewById<Button>(R.id.btnStartGame).setOnClickListener {

            val action = HomeFragmentDirections.actionHomeFragmentToGameViewPagerFragment()
            findNavController().navigate(action)
        }
    }


}