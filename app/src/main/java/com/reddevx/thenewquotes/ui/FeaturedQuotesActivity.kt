package com.reddevx.thenewquotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.QuotesPagerAdapter
import com.reddevx.thenewquotes.models.Quote

class FeaturedQuotesActivity : AppCompatActivity() {

    private lateinit var quoteViewPager:ViewPager2
    private lateinit var viewPagerAdapter:QuotesPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_quotes)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.featured_quotes_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val quoteList = intent.getParcelableArrayListExtra<Quote>(MainActivity.Constants.QUOTE_LIST_KEY) as ArrayList<Quote>
        val currentPosition = intent.getIntExtra(MainActivity.Constants.QUOTE_POSITION_KEY,-1)
        quoteViewPager = findViewById(R.id.quote_view_pager)
        viewPagerAdapter = QuotesPagerAdapter(quoteList)

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




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.quote_page_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


}