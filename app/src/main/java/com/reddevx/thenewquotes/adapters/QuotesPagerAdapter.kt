package com.reddevx.thenewquotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote

class QuotesPagerAdapter(private val quoteList:ArrayList<Quote> ) :
    RecyclerView.Adapter<QuotesPagerAdapter.QuotesPagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesPagerViewHolder {
        return QuotesPagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.featured_quote_page,parent,false))
    }

    override fun onBindViewHolder(holder: QuotesPagerViewHolder, position: Int) {
        holder.apply {
            Glide.with(itemView).load(quoteList[position].imageUrl).into(quoteImg)
            quoteTextTv.text = quoteList[position].quoteText
        }
    }

    override fun getItemCount(): Int = quoteList.size

    inner class QuotesPagerViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val quoteImg:ImageView
        val quoteTextTv:TextView

        init {
            quoteImg = itemView.findViewById(R.id.featured_quote_page_img)
            quoteTextTv = itemView.findViewById(R.id.featured_quote_page_tv)
        }

    }


}