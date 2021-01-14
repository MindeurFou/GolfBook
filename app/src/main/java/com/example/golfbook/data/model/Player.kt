package com.example.golfbook.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
    var name: String?,
    var avatarResourceId: Int,
    val isRealUser: Boolean,
    var par: Int? = null
) : Parcelable