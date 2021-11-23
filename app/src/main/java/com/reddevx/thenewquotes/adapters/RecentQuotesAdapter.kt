package com.reddevx.thenewquotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class RecentQuotesAdapter(private val recentQuotesList:ArrayList<Quote>, private val listener: QuoteInteraction? = null) :
    RecyclerView.Adapter<RecentQuotesAdapter.RecentQuotesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentQuotesViewHolder {
        return RecentQuotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recent_quote_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecentQuotesViewHolder, position: Int) {
        holder.apply {
            Glide.with(itemView).load(recentQuotesList[position].imageUrl).into(recentQuoteImage)
            quoteTextTv.text = recentQuotesList[position].quoteText
            quoteCategoryTv.text = recentQuotesList[position].category
        }
    }

    override fun getItemCount(): Int = recentQuotesList.size

    inner class RecentQuotesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val recentQuoteImage:ImageView
        val quoteTextTv:TextView
        val quoteCategoryTv:TextView
        val shareBtn:ImageButton

        init {
            recentQuoteImage = itemView.findViewById(R.id.recent_quote_image)
            quoteTextTv = itemView.findViewById(R.id.recent_quote_text_tv)
            quoteCategoryTv = itemView.findViewById(R.id.recent_quote_category_tv)
            shareBtn = itemView.findViewById(R.id.recent_quote_share_btn)
            itemView.setOnClickListener(this)


        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener?.onQuoteClick(recentQuotesList, adapterPosition)
            }
        }

    }


}