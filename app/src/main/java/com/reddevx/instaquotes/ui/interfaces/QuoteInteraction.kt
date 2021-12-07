package com.reddevx.instaquotes.ui.interfaces

import com.reddevx.instaquotes.models.Category
import com.reddevx.instaquotes.models.Quote

interface QuoteInteraction {
    fun onQuoteClick(quotes:ArrayList<Quote>, position: Int)
    fun onCategoryClick(category: Category, position:Int)
    fun onViewAllTvClick(quotes:ArrayList<Quote>, position:Int, sectionKey:String)
}