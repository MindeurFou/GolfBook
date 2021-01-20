package com.example.golfbook.ui.compoundedComponents

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.golfbook.R
import com.example.golfbook.data.model.Player

class PlayerPreviewItem(context: Context, player: Player): LinearLayout(context) {

    init {
        val view = inflate(context, R.layout.item_player_preview, this)

        view.findViewById<ImageView>(R.id.imageAvatarPreview).setImageDrawable(ContextCompat.getDrawable(context, player.drawableResourceId))
        view.findViewById<TextView>(R.id.nameAvatarPreview).text = player.name
    }
}