package com.reddevx.thenewquotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class QuotesAdapter(private val data:ArrayList<Quote>, private val listener:QuoteInteraction? = null) :
    RecyclerView.Adapter<QuotesAdapter.FeaturedQuotesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedQuotesViewHolder = FeaturedQuotesViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.latest_quote_item,parent,false))

    override fun onBindViewHolder(holder: FeaturedQuotesViewHolder, position: Int) {
        holder.apply {

            quote.text = data[position].quoteText

                Glide.with(holder.itemView)
                    .load(data[position].imageUrl)
                    .into(quoteImage)

            }




    }

    override fun getItemCount(): Int = data.size


    inner class FeaturedQuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener{
        val quote:TextView
        val quoteImage:ImageView

        init {
            quote = itemView.findViewById(R.id.quote_tv)
            quoteImage = itemView.findViewById(R.id.quote_image)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION)
            listener?.onQuoteClick(data,bindingAdapterPosition)
        }

    }



}