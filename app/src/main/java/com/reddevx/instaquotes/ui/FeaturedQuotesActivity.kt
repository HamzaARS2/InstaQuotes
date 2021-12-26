package com.reddevx.instaquotes.ui

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.reddevx.instaquotes.AdManager
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.adapters.QuotesPagerAdapter
import com.reddevx.instaquotes.database.DatabaseManager
import com.reddevx.instaquotes.models.Quote
import com.reddevx.instaquotes.ui.interfaces.FavoriteListener
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class FeaturedQuotesActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var quoteViewPager: ViewPager2
    private lateinit var viewPagerAdapter: QuotesPagerAdapter
    private lateinit var counterTv: TextView
    private lateinit var heartCheckBox: CheckBox

    private lateinit var quoteList: ArrayList<Quote>
    private lateinit var currentQuote: Quote

    private val db = DatabaseManager.invoke(this)!!

    private lateinit var adManger: AdManager

    private var quotePosition: Int = -1

    companion object {
        private var mListener: FavoriteListener? = null
        private var sListener: FavoriteListener? = null
        private var tListener: FavoriteListener? = null

        fun setOnFavoriteClickListener(listener: FavoriteListener) {
            mListener = listener
        }

        fun setOnFavoriteSecondClickListener(listener: FavoriteListener) {
            sListener = listener
        }

        fun setOnFavoriteThirdClickListener(listener: FavoriteListener) {
            tListener = listener
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



        quoteList =
            intent.getParcelableArrayListExtra<Quote>(MainActivity.Constants.QUOTE_LIST_KEY) as ArrayList<Quote>
        val currentPosition = intent.getIntExtra(MainActivity.Constants.QUOTE_POSITION_KEY, -1)
        currentQuote = quoteList[currentPosition]

        viewPagerAdapter = QuotesPagerAdapter(quoteList, this)

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
            setCurrentItem(currentPosition, false)

        }

        heartCheckBox.isChecked = isFavorite(quoteList[currentPosition].quoteText)


        counterTv.text = "${currentPosition + 1} / ${quoteList.size}"
        quotePosition = currentPosition
        quoteViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                quotePosition = position
                currentQuote = quoteList[position]
                counterTv.text = "${quotePosition + 1} / ${quoteList.size}"
                heartCheckBox.isChecked = isFavorite(currentQuote.quoteText)
            }
        })

        heartCheckBox.setOnClickListener(this)
        adManger = AdManager(this)
        adManger.showInterstitialAd()


    }


    private fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    private fun isFavorite(quoteText: String): Boolean {
        db.open()
        val result = db.isFromFavorites(quoteText)
        db.close()
        return result
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quote_page_menu, menu)
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
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "${quoteToShare} \n https://play.google.com/store/apps/details?id=$packageName"
                        )
                    }
                }
                startActivity(Intent.createChooser(intent, "Quote App"))
                return true
            }
            R.id.page_menu_item_copy -> {
                val quoteToCopy = quoteList[quotePosition].quoteText
                val clipBoard: ClipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("Quote", quoteToCopy)
                clipBoard.setPrimaryClip(clip)
                Toast.makeText(this, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
            }
//            R.id.page_menu_download_item -> {
//                // Try to use IntentService to download in background
//               // saveImage()
//            }

            R.id.page_menu_item_wallpaper -> {
                showToast("Please wait ..")
                val loading = ImageLoader(this)
                val request = ImageRequest.Builder(this)
                    .data(currentQuote.imageUrl)
                    .build()
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        val result = (loading.execute(request) as SuccessResult).drawable
                        val bitmap = (result as BitmapDrawable).bitmap
                        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                        wallpaperManager.setBitmap(bitmap)
                        withContext(Dispatchers.Main) {
                            showToast("Wallpaper successfully set!")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            showToast(e.message.toString())
                        }
                    }
                }
            }

        }
        return true
    }

    override fun onClick(view: View?) {
        val checkBox = view as CheckBox
        db.open()
        if (checkBox.isChecked) {
            if (db.insert(currentQuote)) {
                showToast("This quote is added to Favorite list!")
                setAddListeners()
            } else showToast("Something went wrong!")
        } else {
            if (db.delete(currentQuote)) {
                showToast("This quote is removed from Favorite list!")
                setRemoveListeners()
            } else showToast("Something went wrong!")
        }
        db.close()
    }

    private fun setAddListeners() {
        mListener?.onFavoriteClick()
        sListener?.onFavoriteClick()
        tListener?.onFavoriteClick()
    }

    private fun setRemoveListeners() {
        mListener?.onFavoriteClick()
        sListener?.onFavoriteClick()
        tListener?.onFavoriteClick()
        sListener?.onFavoriteRemoved(quotePosition)
    }


}


