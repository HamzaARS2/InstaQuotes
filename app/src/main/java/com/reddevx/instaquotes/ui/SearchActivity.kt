package com.reddevx.instaquotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.adapters.SearchQuotesAdapter
import com.reddevx.instaquotes.models.Category
import com.reddevx.instaquotes.models.Quote
import com.reddevx.instaquotes.ui.interfaces.FavoriteListener
import com.reddevx.instaquotes.ui.interfaces.QuoteInteraction

class SearchActivity : AppCompatActivity(), QuoteInteraction ,FavoriteListener{
    private lateinit var searchToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var searchRv:RecyclerView
    private lateinit var searchAdapter: SearchQuotesAdapter

    private lateinit var filterableQuotes:ArrayList<Quote>
    private lateinit var allQuotes:ArrayList<Quote>

    val fireStore = FirebaseFirestore.getInstance()

    companion object {
        private var searchListener:FavoriteListener? = null

        fun setOnFavoriteSearchClickListener(listener: FavoriteListener){
            searchListener = listener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchToolbar = findViewById(R.id.search_toolbar)
        searchRv = findViewById(R.id.searchRv)

        filterableQuotes = ArrayList()
        allQuotes = ArrayList()

        searchAdapter = SearchQuotesAdapter(filterableQuotes,allQuotes,listener = searchListener,context = this)
        searchRv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity,LinearLayoutManager.VERTICAL,false)
        }
        searchToolbar.title = ""
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        FeaturedQuotesActivity.setOnFavoriteThirdClickListener(this)
        loadAllQuotes()
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
                return true
            }

        })
        return true
    }

    private fun loadAllQuotes(){
        val mFeaturedColl = fireStore.collection("quotes")
        val mRecentColl = fireStore.collection("recent")
        mFeaturedColl
            .get()
            .addOnSuccessListener(this) { snapShot ->
                for (doc in snapShot!!.documents) {
                    val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                    val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                    val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                    val quote = Quote(imageUrl, quoteText, category)
                    filterableQuotes.add(quote)
                    allQuotes.add(quote)
                    searchAdapter.notifyItemInserted(filterableQuotes.size - 1)
                }
            }
            .addOnFailureListener(this
            ) { e -> Log.e("Listening failed!", "onFailure: $e",) }
        mRecentColl
            .get()
            .addOnSuccessListener(this) { snapShot ->
                for (doc in snapShot!!.documents) {
                    val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                    val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                    val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                    val quote = Quote(imageUrl, quoteText, category)
                    filterableQuotes.add(quote)
                    allQuotes.add(quote)
                    searchAdapter.notifyItemInserted(filterableQuotes.size - 1)
                }
            }
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
        val intent = Intent(this, FeaturedQuotesActivity::class.java)
        intent.apply {
            putParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY, quotes)
            putExtra(MainActivity.Constants.QUOTE_POSITION_KEY, position)
        }
        startActivity(intent)
    }

    override fun onCategoryClick(category: Category, position: Int) {
    }

    override fun onViewAllTvClick(quotes: ArrayList<Quote>, position: Int, sectionKey: String) {
    }

    override fun onFavoriteClick() {
        searchAdapter.notifyChanges()
    }
    override fun onFavoriteRemoved(position: Int) {
    }
}