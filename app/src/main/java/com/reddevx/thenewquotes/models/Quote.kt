package com.reddevx.thenewquotes.models

data class Quote(
    val imageUrl:String,
    val quoteText:String,
    val category:String,
    val isFavorite:Boolean
)
