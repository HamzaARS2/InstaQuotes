package com.reddevx.instaquotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.models.Quote

class QuotesPagerAdapter(private val quoteList:ArrayList<Quote>,val context: Context) :
    RecyclerView.Adapter<QuotesPagerAdapter.QuotesPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesPagerViewHolder {
        return QuotesPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.featured_quote_page, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuotesPagerViewHolder, position: Int) {
        val currentQuote = quoteList[position]
            holder.apply {
                Glide.with(context)
                    .asBitmap()
                    .load(currentQuote.imageUrl)
                    .centerCrop()
                    .into(quoteImg)
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