package com.reddevx.thenewquotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.RecentQuotesAdapter
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote

class CategoryQuotesActivity : AppCompatActivity() {


    private lateinit var quotesRv: RecyclerView
    private lateinit var quotesAdapter: RecentQuotesAdapter
    private lateinit var toolbarTv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_quotes)
        val toolbar = findViewById<Toolbar>(R.id.category_quote_toolbar)
        toolbarTv = toolbar.findViewById(R.id.category_quote_toolbar_tv)
        quotesRv = findViewById(R.id.category_quote_recycler_view)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent != null) {
            prepareData()
        }




    }

    private fun prepareData() {
       if (prepareFeaturedQuotes(intent))
           return
        if (prepareCategories(intent))
            return
        if (prepareRecentQuotes(intent))
            return
    }

    private fun prepareFeaturedQuotes(intent:Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_ONE)) {
            val quoteList:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY)!!
            quotesAdapter = RecentQuotesAdapter(quoteList)
            toolbarTv.text = "Featured Quotes"
            buildRecyclerView(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false))
            return true
        }
        return false
    }


    private fun prepareCategories(intent:Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_TWO)){
            val category = intent.getParcelableExtra<Category>(MainActivity.Constants.CATEGORY_KEY)!!
            quotesAdapter = RecentQuotesAdapter(category.categoryQuotes)
            toolbarTv.text = category.categoryName
            buildRecyclerView(GridLayoutManager(this@CategoryQuotesActivity, 2))
            return true
        }
        return false
    }

    private fun prepareRecentQuotes(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_THREE)){
            val recentQuotes:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY)!!
            quotesAdapter = RecentQuotesAdapter(recentQuotes)
            toolbarTv.text = "Recent Quotes"
            buildRecyclerView(StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL))
            return true
        }
        return false
    }

    private fun buildRecyclerView(rvLayoutManger:RecyclerView.LayoutManager){
        quotesRv.apply {
            adapter = quotesAdapter
            layoutManager = rvLayoutManger
            hasFixedSize()
        }
    }








}