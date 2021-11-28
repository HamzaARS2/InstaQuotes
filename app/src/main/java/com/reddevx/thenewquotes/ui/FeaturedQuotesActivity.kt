package com.reddevx.thenewquotes.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.QuotesPagerAdapter
import com.reddevx.thenewquotes.models.Quote
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class FeaturedQuotesActivity : AppCompatActivity() {

    private lateinit var quoteViewPager:ViewPager2
    private lateinit var viewPagerAdapter:QuotesPagerAdapter
    private lateinit var counterTv:TextView
    private lateinit var quoteList:ArrayList<Quote>
    private var quotePosition:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_quotes)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.featured_quotes_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        counterTv = findViewById(R.id.page_menu_counter_tv)

        window.setFormat(PixelFormat.RGBA_8888)



        quoteList  = intent.getParcelableArrayListExtra<Quote>(MainActivity.Constants.QUOTE_LIST_KEY) as ArrayList<Quote>
        val currentPosition = intent.getIntExtra(MainActivity.Constants.QUOTE_POSITION_KEY,-1)
        quoteViewPager = findViewById(R.id.quote_view_pager)
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
        counterTv.text = "${currentPosition+1} / ${quoteList.size}"
        quotePosition = currentPosition+1
        quoteViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                quotePosition = position
                counterTv.text = "${quotePosition+1} / ${quoteList.size}"
            }

        })





    }

    private fun saveImage() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)

        var fileOutputStream:FileOutputStream? = null
        val file = getDir()
        if (!file.exists() && !file.mkdir()){
            file.mkdirs()
        }

        val simpleDateFormat = SimpleDateFormat("yyyymmsshhmmss")
        val date = simpleDateFormat.format(Date())
        val name = "QuoteIMG$date.jpg"
        val fileName = "${file.absolutePath}/$name"
        val newFile = File(fileName)

        val bitmap = getBitmap(quoteList[quotePosition].imageUrl)


        try {
            if (bitmap != null){
                fileOutputStream = FileOutputStream(newFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Something went wrong! please try again", Toast.LENGTH_SHORT).show()
            }
            fileOutputStream?.flush()
            fileOutputStream?.close()
        }catch (e:IOException){
            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()

        }

        updateGallery(file)

    }

    fun updateGallery(file: File){
        MediaScannerConnection.scanFile(this, arrayOf(file.toString()),null,null)
    }

    private fun getDir() : File {
        val file: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(file,"QuoteImage")
    }

    private fun getBitmap(imageUrl:String) : Bitmap? {
        try {
            val url = URL(imageUrl)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            return bitmap
        } catch (e:IOException){
            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quote_page_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

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
            R.id.page_menu_download_item -> {
                // Try to use IntentService to download in background
                //saveImage()
            }

        }
        return true
    }




}


