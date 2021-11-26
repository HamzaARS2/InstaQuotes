package com.reddevx.thenewquotes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class RecentQuotesAdapter(private var recentQuotesList:ArrayList<Quote> = arrayListOf(), private val listener: QuoteInteraction? = null,private val isFavorties:Boolean = false) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val FAVORITES = 0
    val NOT_FAVORTIES = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FAVORITES) {
            return FavoriteQuotesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.favorite_quote_item,parent,false))
        }

        return RecentQuotesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recent_quote_item, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecentQuotesViewHolder) {
            holder.apply {
                Glide.with(itemView)
                    .load(recentQuotesList[position].imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(recentQuoteImage)

                quoteTextTv.text = recentQuotesList[position].quoteText
                quoteCategoryTv.text = recentQuotesList[position].category
            }
        } else {
            (holder as FavoriteQuotesViewHolder).apply {
                Glide.with(itemView)
                    .load(recentQuotesList[position].imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(favortieQuoteImage)
                favortieQuoteTv.text = recentQuotesList[position].quoteText
                favortieQuoteCategoryTv.text = recentQuotesList[position].category
            }
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

    inner class FavoriteQuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val favortieQuoteImage:ImageView
        val favortieQuoteTv:TextView
        val favortieQuoteCategoryTv:TextView
        val favortieShareBtn:ImageButton

        init {
            favortieQuoteImage = itemView.findViewById(R.id.favorite_quote_img)
            favortieQuoteTv = itemView.findViewById(R.id.favorite_quote_tv)
            favortieQuoteCategoryTv = itemView.findViewById(R.id.favorite_quote_category_tv)
            favortieShareBtn = itemView.findViewById(R.id.favorite_quote_share_btn)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (adapterPosition != RecyclerView.NO_POSITION)
                listener?.onQuoteClick(recentQuotesList,adapterPosition)
        }
    }

     fun setData(quotes:ArrayList<Quote>) {
        recentQuotesList.clear()
        recentQuotesList = quotes
    }

    override fun getItemViewType(position: Int): Int {
        return if (isFavorties)
            FAVORITES
        else
            NOT_FAVORTIES
    }


}