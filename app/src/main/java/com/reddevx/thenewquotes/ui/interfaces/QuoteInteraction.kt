package com.reddevx.thenewquotes.ui.interfaces

import android.view.View
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote

interface QuoteInteraction {
    fun onQuoteClick(quotes:ArrayList<Quote>, position: Int)
    fun onCategoryClick(category: Category, position:Int)
    fun onViewAllTvClick(quotes:ArrayList<Quote>, position:Int, sectionKey:String)
}