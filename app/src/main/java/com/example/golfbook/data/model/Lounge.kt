package com.example.golfbook.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lounge(
        val loungeNumber: Int,
        var players: List<Player>? = null,
        var course: String? = null
) : Parcelable