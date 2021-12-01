package com.reddevx.thenewquotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.SearchQuotesAdapter
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class SearchActivity : AppCompatActivity(), QuoteInteraction {
    private lateinit var searchToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var searchRv:RecyclerView
    private lateinit var searchAdapter: SearchQuotesAdapter
    private lateinit var noDataView:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchToolbar = findViewById(R.id.search_toolbar)
        searchRv = findViewById(R.id.searchRv)
        noDataView = findViewById(R.id.search_noData_LL)
        searchAdapter = SearchQuotesAdapter(getRecentQuotes(),getRecentQuotes(),this)
        searchRv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity,LinearLayoutManager.VERTICAL,false)
        }
        searchToolbar.title = ""
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_quotes_menu,menu)
        val item = menu?.findItem(R.id.search_search_menu_item)
        val searchView: SearchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.filter.filter(newText)
                if (newText?.trim()!!.isEmpty())
                    noDataView.visibility = View.VISIBLE
                else
                    noDataView.visibility = View.GONE
                return true
            }

        })
        return true
    }


    private fun getRecentQuotes(): ArrayList<Quote> {
        return arrayListOf(
            Quote(
                "https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.",
                "Age",
                false
            ),
            Quote(
                "https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "Can't you see that I'm only advising you to beg yourself not to be so dumb?",
                "Funny"
            ),
            Quote(
                "https://images.pexels.com/photos/235986/pexels-photo-235986.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "Can't you see that I'm only advising you to beg yourself not to be so dumb?",
                "Funny"
            ),
            Quote(
                "https://images.pexels.com/photos/235615/pexels-photo-235615.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "Can't you see that I'm only advising you to beg yourself not to be so dumb?",
                "Funny"
            ),
            Quote(
                "https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "Can't you see that I'm only advising you to beg yourself not to be so dumb?",
                "Alone"
            ),
            Quote(
                "https://images.pexels.com/photos/2307562/pexels-photo-2307562.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "Can't you see that I'm only advising you to beg yourself not to be so dumb?",
                "Friendship"
            ),
            Quote(
                "https://images.pexels.com/photos/236214/pexels-photo-236214.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "Can't you see that I'm only advising you to beg yourself not to be so dumb?",
                "Age"
            ),
            Quote(
                "https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.",
                "Age",
                false
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return true
    }

    override fun onQuoteClick(quotes: ArrayList<Quote>, position: Int) {
    }

    override fun onCategoryClick(category: Category, position: Int) {
    }

    override fun onViewAllTvClick(quotes: ArrayList<Quote>, position: Int, sectionKey: String) {
    }
}