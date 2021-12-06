package com.reddevx.thenewquotes.ui

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.QuotesPagerAdapter
import com.reddevx.thenewquotes.database.DatabaseManager
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.FavoriteListener
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class FeaturedQuotesActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var quoteViewPager:ViewPager2
    private lateinit var viewPagerAdapter:QuotesPagerAdapter
    private lateinit var counterTv:TextView
    private lateinit var heartCheckBox: CheckBox

    private lateinit var quoteList:ArrayList<Quote>
    private lateinit var currentQuote:Quote

    private val db = DatabaseManager.invoke(this)!!

    private var quotePosition:Int = -1

    companion object {
        private lateinit var mListener: FavoriteListener
        fun setOnFavoriteClickListener(listener: FavoriteListener){
            mListener = listener
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_quotes)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.featured_quotes_toolbar)
        heartCheckBox = findViewById(R.id.page_favorite_item)
        quoteViewPager = findViewById(R.id.quote_view_pager)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        counterTv = findViewById(R.id.page_menu_counter_tv)


        quoteList  = intent.getParcelableArrayListExtra<Quote>(MainActivity.Constants.QUOTE_LIST_KEY) as ArrayList<Quote>
        val currentPosition = intent.getIntExtra(MainActivity.Constants.QUOTE_POSITION_KEY,-1)
        currentQuote = quoteList[currentPosition]

        viewPagerAdapter = QuotesPagerAdapter(quoteList,this)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(70))


        quoteViewPager.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(compositePageTransformer)
            setCurrentItem(currentPosition,false)

        }

        heartCheckBox.isChecked = isFavorite(quoteList[currentPosition].quoteText)


        counterTv.text = "${currentPosition+1} / ${quoteList.size}"
        quotePosition = currentPosition+1
        quoteViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                quotePosition = position
                currentQuote = quoteList[position]
                counterTv.text = "${quotePosition+1} / ${quoteList.size}"
                heartCheckBox.isChecked = isFavorite(currentQuote.quoteText)

            }

        })

        heartCheckBox.setOnClickListener(this)

    }

    private fun showToast(content:String){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    private fun isFavorite(quoteText:String) : Boolean{
        db.open()
        val result = db.isFromFavorites(quoteText)
        db.close()
        return result
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quote_page_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.page_menu_item_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.apply {
                    type = "text/plain"
                    if (quotePosition != -1) {
                        val quoteToShare = quoteList[quotePosition].quoteText
                        putExtra(Intent.EXTRA_TEXT,"${quoteToShare} \n https://play.google.com/store/apps/details?id=com.rm.instaquotes")
                    }
                }
                startActivity(Intent.createChooser(intent,"Quote App"))
                return true
            }
            R.id.page_menu_item_copy -> {
                val quoteToCopy = quoteList[quotePosition].quoteText
                val clipBoard:ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip:ClipData = ClipData.newPlainText("Quote",quoteToCopy)
                clipBoard.setPrimaryClip(clip)
                Toast.makeText(this, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
            }
//            R.id.page_menu_download_item -> {
//                // Try to use IntentService to download in background
//               // saveImage()
//            }

            R.id.page_menu_item_wallpaper -> {

              val loading = ImageLoader(this)
              val request = ImageRequest.Builder(this)
                  .data(currentQuote.imageUrl)
                  .build()
                GlobalScope.launch {
                    val result = (loading.execute(request) as SuccessResult).drawable
                    val bitmap = (result as BitmapDrawable).bitmap
                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    wallpaperManager.setBitmap(bitmap)
                    GlobalScope.launch(Dispatchers.Main) {
                        showToast("Wallpaper successfully set!")
                    }
                }
            }

        }
        return true
    }

    override fun onClick(view: View?) {
        val checkBox = view as CheckBox
        db.open()
        if (checkBox.isChecked){
            if (db.insert(currentQuote)) {
                showToast("This quote is added to Favorite list!")
                mListener.onFavoriteClick()
            }
            else showToast("Something went wrong!")
        }else {
            if (db.delete(currentQuote)) {
                showToast("This quote is removed from Favorite list!")
                mListener.onFavoriteClick()
            }
            else showToast("Something went wrong!")
        }
        db.close()
    }


}


