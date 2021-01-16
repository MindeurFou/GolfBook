package com.example.golfbook.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.example.golfbook.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

object ViewExtensions {

    fun ImageView.findDrawableResourceId() : Int {

        return when (this.id) {

            R.id.man1View -> R.drawable.man1
            R.id.man2View -> R.drawable.man2
            R.id.man3View -> R.drawable.man3
            R.id.man4View -> R.drawable.man4
            R.id.man5View -> R.drawable.man5
            R.id.man6View -> R.drawable.man6
            R.id.man7View -> R.drawable.man7
            R.id.man8View -> R.drawable.man8
            R.id.woman1View -> R.drawable.woman1
            R.id.woman2View -> R.drawable.woman2
            R.id.woman3View -> R.drawable.woman3
            R.id.woman4View -> R.drawable.woman4
            R.id.woman5View -> R.drawable.woman5
            R.id.woman6View -> R.drawable.woman6
            R.id.woman7View -> R.drawable.woman7
            R.id.woman8View -> R.drawable.woman8
            else -> R.drawable.man1
        }

    }

    @ExperimentalCoroutinesApi
    fun TextInputEditText.textChanges(): Flow<CharSequence?> {

        return callbackFlow<CharSequence?> {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    offer(s)
                }
            }
            addTextChangedListener(listener)
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }
}