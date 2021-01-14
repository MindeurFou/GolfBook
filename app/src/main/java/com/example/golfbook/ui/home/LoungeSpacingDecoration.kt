package com.example.golfbook.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LoungeSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)

        outRect.left = if (position != 0)
            8
        else
            1

    }

}