package com.example.golfbook.extensions

import android.content.Context
import android.widget.Toast
import java.lang.Exception

object ExceptionExtensions {

    fun Exception.toast(context: Context) {
        Toast.makeText(context, this.message, Toast.LENGTH_LONG).show()
    }
}