package com.reddevx.thenewquotes.adapters

import android.widget.Filter
import android.widget.Filterable
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.SearchActivity
import com.reddevx.thenewquotes.ui.interfaces.FavoriteListener

class SearchQuotesAdapter(private val quotes:ArrayList<Quote>,
                          private val allQuotes: ArrayList<Quote>,
                          val listener: FavoriteListener? = null,
                          val context: SearchActivity)
    : RecentQuotesAdapter(recentQuotesList = quotes ,listener = context,favListener = listener,context = context)
    , Filterable {



    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredQuotes = ArrayList<Quote>()
            if (constraint.toString().isEmpty()) {
                filteredQuotes.addAll(allQuotes)
            } else {
                for (quote in allQuotes) {
                    if (quote.quoteText.lowercase().contains(constraint.toString().lowercase())) {
                        filteredQuotes.add(quote)
                    }
                }
            }

            val result = FilterResults()
            result.values = filteredQuotes
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            quotes.clear()
            quotes.addAll(results?.values as Collection<Quote>)
            notifyDataSetChanged()

        }
    }

    override fun notifyChanges(){
        notifyItemRangeChanged(0,quotes.size)
    }


    override fun getFilter(): Filter {
        return filter
    }

}