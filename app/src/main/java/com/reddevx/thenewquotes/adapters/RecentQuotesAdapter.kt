package com.reddevx.thenewquotes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

open class RecentQuotesAdapter(
    private var recentQuotesList: ArrayList<Quote> = arrayListOf(),
    private val listener: QuoteInteraction? = null,
    private val isFavorties: Boolean = false,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val FAVORITES = 0
    val NOT_FAVORTIES = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FAVORITES) {
            return FavoriteQuotesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.favorite_quote_item, parent, false)
            )
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



    inner class RecentQuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val recentQuoteImage: ImageView
        val quoteTextTv: TextView
        val quoteCategoryTv: TextView
        val shareBtn: ImageButton
        val recentHeartBox: CheckBox

        init {
            recentQuoteImage = itemView.findViewById(R.id.recent_quote_image)
            quoteTextTv = itemView.findViewById(R.id.recent_quote_text_tv)
            quoteCategoryTv = itemView.findViewById(R.id.recent_quote_category_tv)
            shareBtn = itemView.findViewById(R.id.recent_quote_share_btn)
            recentHeartBox = itemView.findViewById(R.id.recent_heart_img_btn)
            itemView.setOnClickListener(this)
            recentHeartBox.setOnClickListener(this)
            shareBtn.setOnClickListener(this)


        }

        override fun onClick(v: View?) {
            when (v) {
                itemView -> {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                        listener?.onQuoteClick(recentQuotesList, bindingAdapterPosition)
                }
                recentHeartBox -> {
                    if (recentHeartBox.isChecked)
                        Toast.makeText(
                            context,
                            "This quote is added to Favorite list!",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(
                            context,
                            "This quote is removed from Favorite list!",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                shareBtn -> {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.apply {
                            type = "text/plain"
                            val quoteToShare = recentQuotesList[bindingAdapterPosition].quoteText
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${quoteToShare} \n https://play.google.com/store/apps/details?id=com.rm.instaquotes"
                            )
                        }
                        startActivity(context,Intent.createChooser(intent,"Install App!"),null)
                    }

                }
            }

        }

    }

    inner class FavoriteQuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val favortieQuoteImage: ImageView
        val favortieQuoteTv: TextView
        val favortieQuoteCategoryTv: TextView
        val favortieShareBtn: ImageButton

        init {
            favortieQuoteImage = itemView.findViewById(R.id.favorite_quote_img)
            favortieQuoteTv = itemView.findViewById(R.id.favorite_quote_tv)
            favortieQuoteCategoryTv = itemView.findViewById(R.id.favorite_quote_category_tv)
            favortieShareBtn = itemView.findViewById(R.id.favorite_quote_share_btn)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                listener?.onQuoteClick(recentQuotesList, bindingAdapterPosition)
        }
    }



    override fun getItemViewType(position: Int): Int {
        return if (isFavorties)
            FAVORITES
        else
            NOT_FAVORTIES
    }


}