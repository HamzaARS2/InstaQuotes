package com.reddevx.thenewquotes.models

import android.os.Parcelable
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val categoryImage: Int,
    val categoryName:String,
    val categoryQuotes:ArrayList<Quote>
) : Parcelable
