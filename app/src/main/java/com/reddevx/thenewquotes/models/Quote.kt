package com.reddevx.thenewquotes.models

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote (
    val imageUrl:String,
    val quoteText:String,
    val category:String,
    val isFavorite:Boolean = false
) : Parcelable
