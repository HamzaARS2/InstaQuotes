package com.reddevx.thenewquotes.adapters

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Quote
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

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

    private fun getBitmap(imageUrl:String) : Bitmap? {

        val url = URL(imageUrl)
        val urlConnection = url.openConnection() as HttpURLConnection
        var bitmap: Bitmap? = null
        try {
            val inputStream = BufferedInputStream(urlConnection.inputStream)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        finally {
            urlConnection.disconnect()
        }
        return bitmap
    }

    private suspend fun downloadImage(imageUrl: String): Bitmap? {
        val url = URL(imageUrl)
        val result: Deferred<Bitmap?> = GlobalScope.async {
            getBitmap(imageUrl)
        }
        val bitmap = result.await()

        return bitmap
    }








//    val loading = ImageLoader(this)
//    val request = ImageRequest.Builder(this)
//        .data(currentQuote.imageUrl)
//        .build()
//    GlobalScope.launch {
//        val result = (loading.execute(request) as SuccessResult).drawable
//        val bitmap = (result as BitmapDrawable).bitmap
//        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
//        wallpaperManager.setBitmap(bitmap)
//        GlobalScope.launch(Dispatchers.Main) {
//            showToast("Wallpaper successfully set!")
//        }
//    }
//}

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