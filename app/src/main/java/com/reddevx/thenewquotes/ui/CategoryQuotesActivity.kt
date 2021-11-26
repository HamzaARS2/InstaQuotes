package com.reddevx.thenewquotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.*
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.CategoryAdapter
import com.reddevx.thenewquotes.adapters.RecentQuotesAdapter
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class CategoryQuotesActivity : AppCompatActivity(), QuoteInteraction {


    private lateinit var quotesRv: RecyclerView
    private lateinit var quotesAdapter: RecentQuotesAdapter
    private lateinit var toolbarTv: TextView
    private lateinit var toolbarDelBtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_quotes)
        val toolbar = findViewById<Toolbar>(R.id.category_quote_toolbar)
        toolbarTv = toolbar.findViewById(R.id.category_quote_toolbar_tv)
        toolbarDelBtn = toolbar.findViewById(R.id.category_quote_toolbar_deleteAll_btn)

        quotesRv = findViewById(R.id.category_quote_recycler_view)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        quotesAdapter = RecentQuotesAdapter(listener = this)
        if (intent != null) {
            prepareData()
        }




    }

    private fun prepareData() {
       if (prepareFeaturedQuotes(intent))
           return
        if (prepareCategory(intent))
            return
        if (prepareRecentQuotes(intent))
            return
        if (prepareCategories(intent))
            return
        if (prepareFavorites(intent))
            return
    }

    private fun prepareFeaturedQuotes(intent:Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_ONE)) {
            val quoteList:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY)!!
            quotesAdapter.setData(quoteList)
            toolbarTv.text = "Featured Quotes"
            toolbarDelBtn.visibility = View.GONE
            buildRecyclerView(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false))
            return true
        }
        return false
    }


    private fun prepareCategory(intent:Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_TWO)){
            val category = intent.getParcelableExtra<Category>(MainActivity.Constants.CATEGORY_KEY)!!
            quotesAdapter.setData(category.categoryQuotes)
            toolbarTv.text = category.categoryName
            toolbarDelBtn.visibility = View.GONE
            buildRecyclerView(GridLayoutManager(this@CategoryQuotesActivity, 2))
            return true
        }
        return false
    }

    private fun prepareRecentQuotes(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_THREE)){
            val recentQuotes:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY)!!
            quotesAdapter.setData(recentQuotes)
            toolbarTv.text = "Recent Quotes"
            toolbarDelBtn.visibility = View.GONE
            buildRecyclerView(StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL))
            return true
        }
        return false
    }

    private fun prepareCategories(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_NAV_CATEGORIES)){
            val categories:ArrayList<Category> = intent.getParcelableArrayListExtra(MainActivity.Constants.CATEGORIES_KEY)!!
            val categoryAdapter = CategoryAdapter(categories,this)
            toolbarTv.text = "Categories"
            buildRecyclerView(GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false),categoryAdapter)
            return true
        }
        return false
    }

    private fun prepareFavorites(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_FAVORITES)) {
            val favorites:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.FAVORITES_KEY)!!
            quotesAdapter = RecentQuotesAdapter(favorites,listener = this,isFavorties = true)
            toolbarTv.text = "Favorites"
            toolbarDelBtn.visibility = View.VISIBLE
            val favoritesRv = buildRecyclerView(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false))
            favoritesRv.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
            return true
        }
        return false
    }

    private fun buildRecyclerView(rvLayoutManger:RecyclerView.LayoutManager, categoryAdapter:CategoryAdapter? = null) : RecyclerView {

        return quotesRv.apply {
            if (categoryAdapter == null)
            adapter = quotesAdapter
            else
                adapter = categoryAdapter
            layoutManager = rvLayoutManger
            hasFixedSize()

        }

    }

    override fun onQuoteClick(quotes: ArrayList<Quote>, position: Int) {
        Log.i("CategoryActivity", "onClick: OnClick called")
        val intent = Intent(this,FeaturedQuotesActivity::class.java)
        intent.apply {
            putParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY,quotes)
            putExtra(MainActivity.Constants.QUOTE_POSITION_KEY,position)
        }
        startActivity(intent)
    }

    override fun onCategoryClick(category: Category, position: Int) {
        Toast.makeText(this, category.categoryName, Toast.LENGTH_SHORT).show()
    }

    override fun onViewAllTvClick(quotes: ArrayList<Quote>, position: Int, sectionKey: String){}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }


}