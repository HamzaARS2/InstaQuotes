package com.reddevx.instaquotes.adapters

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
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.database.DatabaseManager
import com.reddevx.instaquotes.models.Quote
import com.reddevx.instaquotes.ui.interfaces.FavoriteListener
import com.reddevx.instaquotes.ui.interfaces.QuoteInteraction

open class RecentQuotesAdapter(
    private var recentQuotesList: ArrayList<Quote> = arrayListOf(),
    private val listener: QuoteInteraction? = null,
    private var favListener:FavoriteListener? = null,
    private val isFavorties: Boolean = false,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val FAVORITES = 0
    val NOT_FAVORTIES = 1


    private val db = DatabaseManager.invoke(context)!!

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
        val quote = recentQuotesList[position]
        if (holder is RecentQuotesViewHolder) {
            holder.apply {
                Glide.with(itemView)
                    .load(quote.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(recentQuoteImage)
                quoteTextTv.text = quote.quoteText
                quoteCategoryTv.text = quote.category
                recentHeartBox.isChecked = isFavorite(quote.quoteText)


            }
        } else {
            (holder as FavoriteQuotesViewHolder).apply {
                Glide.with(itemView)
                    .load(quote.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(favortieQuoteImage)
                favortieQuoteTv.text = quote.quoteText
                favortieQuoteCategoryTv.text = quote.category

            }
        }
    }

    override fun getItemCount(): Int = recentQuotesList.size

    private fun getCurrentQuote(position: Int) :Quote = recentQuotesList[position]

    open fun notifyChanges(){
        notifyItemRangeChanged(0,recentQuotesList.size)
    }




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
                        addQuote(bindingAdapterPosition)
                    else removeQuote(bindingAdapterPosition)
                }
                shareBtn -> shareQuote(bindingAdapterPosition)
            }

        }

        private fun addQuote(position: Int){
            val result = addFavorite(getCurrentQuote(position))
            if (position != RecyclerView.NO_POSITION)
                favListener?.onFavoriteClick()
            if (result) showToast("This quote is added to Favorite list!")
            else showToast("Something went wrong!")
        }

        private fun removeQuote(position: Int){
            val result = deleteFavorite(getCurrentQuote(position))
            if (position != RecyclerView.NO_POSITION)
                favListener?.onFavoriteClick()
            if (result) showToast("This quote is removed from Favorite list!")
            else showToast("Something went wrong!")

        }

    }

    inner class FavoriteQuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val favortieQuoteImage: ImageView
        val favortieQuoteTv: TextView
        val favortieQuoteCategoryTv: TextView
        val favortieShareBtn: ImageButton
        val heartCheckBox:CheckBox

        init {
            favortieQuoteImage = itemView.findViewById(R.id.favorite_quote_img)
            favortieQuoteTv = itemView.findViewById(R.id.favorite_quote_tv)
            favortieQuoteCategoryTv = itemView.findViewById(R.id.favorite_quote_category_tv)
            favortieShareBtn = itemView.findViewById(R.id.favorite_quote_share_btn)
            heartCheckBox = itemView.findViewById(R.id.favorite_quote_heart_checkBox)
            heartCheckBox.setOnClickListener(this)
            favortieShareBtn.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v){
                itemView -> {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                        listener?.onQuoteClick(recentQuotesList, bindingAdapterPosition)
                }
                heartCheckBox -> removeQuote(bindingAdapterPosition)
                favortieShareBtn -> shareQuote(bindingAdapterPosition)
            }
        }

         private fun removeQuote(position: Int){
            val result = deleteFavorite(getCurrentQuote(position))
            if (result) {
                favListener?.onFavoriteClick()
                showToast("This quote is removed from Favorite list!")
                recentQuotesList.removeAt(position)
                notifyItemRemoved(position)
            }
            else showToast("Something went wrong!")

        }
    }

    fun removeAll(){
        val size = recentQuotesList.size
        recentQuotesList.clear()
        notifyItemRangeRemoved(0,size)
        favListener?.onFavoriteClick()
    }



    private fun shareQuote(position: Int){
        if (position != RecyclerView.NO_POSITION) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.apply {
                type = "text/plain"
                val quoteToShare = recentQuotesList[position].quoteText
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${quoteToShare} \n https://play.google.com/store/apps/details?id=${context.packageName}"
                )
            }
            startActivity(context,Intent.createChooser(intent,"Install App!"),null)
        }
    }



    override fun getItemViewType(position: Int): Int {
        return if (isFavorties)
            FAVORITES
        else
            NOT_FAVORTIES
    }

    private fun addFavorite(quote: Quote) : Boolean{
        db.open()
        val result = db.insert(quote)
        db.close()
        return result
    }

    private fun deleteFavorite(quote: Quote) : Boolean{
        db.open()
        val result = db.delete(quote)
        db.close()
        return result
    }

    private fun showToast(content:String){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

    private fun isFavorite(quoteText:String) : Boolean {
        db.open()
        val favorites = db.getUserFavorites()
        db.close()
        for (value in favorites){
            if (value.quoteText == quoteText)
                return true
        }
        return false
    }

    fun remove(position: Int){
        recentQuotesList.removeAt(position)
        notifyItemRemoved(position)
    }


}