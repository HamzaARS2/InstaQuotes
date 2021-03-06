package com.reddevx.instaquotes.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote (
    val imageUrl:String = "",
    val quoteText:String = "",
    val category:String = "",
    val isFavorite:Boolean = false
) : Parcelable
