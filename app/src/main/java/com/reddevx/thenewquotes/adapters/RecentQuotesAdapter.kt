package com.reddevx.thenewquotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote

class RecentQuotesAdapter(val recentQuotesList:ArrayList<Quote>, val context:Context) :
    RecyclerView.Adapter<RecentQuotesAdapter.RecentQuotesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentQuotesViewHolder {
        return RecentQuotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recent_quote_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecentQuotesViewHolder, position: Int) {
        holder.apply {
            Glide.with(context).load(recentQuotesList[position].imageUrl).into(recentQuoteImage)
            quoteTextTv.text = recentQuotesList[position].quoteText
            quoteCategoryTv.text = recentQuotesList[position].category
        }
    }

    override fun getItemCount(): Int = recentQuotesList.size

    class RecentQuotesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val recentQuoteImage:ImageView
        val quoteTextTv:TextView
        val quoteCategoryTv:TextView

        init {
            recentQuoteImage = itemView.findViewById(R.id.recent_quote_image)
            quoteTextTv = itemView.findViewById(R.id.recent_quote_tv)
            quoteCategoryTv = itemView.findViewById(R.id.recent_quote_category_tv)
        }

    }
}