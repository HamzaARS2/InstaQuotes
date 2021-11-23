package com.reddevx.thenewquotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.MainAdapter
import com.reddevx.thenewquotes.adapters.QuotesAdapter
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class MainActivity : AppCompatActivity() ,QuoteInteraction {

    object Constants {
        const val QUOTE_LIST_KEY:String = "quote_list"
        const val QUOTE_POSITION_KEY = "quote_position"
        const val CATEGORY_POSITION_KEY = "category_position"
        const val CATEGORY_KEY = "category_quotes_key"
        const val QUOTES_TYPE_KEY = " which_list_will_display"

        const val FROM_SECTION_ONE = "1"
        const val FROM_SECTION_TWO = "2"
        const val FROM_SECTION_THREE = "3"
    }

    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainRecyclerView: RecyclerView

    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawerLayout:DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.main_drawer_layout)

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainRecyclerView = findViewById(R.id.main_recycler_view)
        mainAdapter = MainAdapter(getQuotes(),getCategories(),getRecentQuotes(),this)
        mainRecyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
            hasFixedSize()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getQuotes() : ArrayList<Quote> {
        return arrayListOf(
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age"),
            Quote("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Funny"),
            Quote("https://images.pexels.com/photos/235986/pexels-photo-235986.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Funny"),
            Quote("https://images.pexels.com/photos/235615/pexels-photo-235615.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Funny"),
            Quote("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Alone"),
            Quote("https://images.pexels.com/photos/2307562/pexels-photo-2307562.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Friendship"),
            Quote("https://images.pexels.com/photos/236214/pexels-photo-236214.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Age"),
            )
    }

    private fun getCategories() : ArrayList<Category> {
        return arrayListOf(
            Category(R.drawable.age_circle,"Age",getQuotes()),
            Category(R.drawable.alone_circle,"Alone",getQuotes()),
            Category(R.drawable.angry_circle,"Angry",getQuotes()),
            Category(R.drawable.family_circle,"Family",getQuotes()),
            Category(R.drawable.friendship_circle,"Friendship",getQuotes()),
            Category(R.drawable.funny_circle,"Funny",getQuotes()),
            Category(R.drawable.life_circle,"Life",getQuotes())
        )
    }

    private fun getRecentQuotes() :ArrayList<Quote> {
        return arrayListOf(
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Funny"),
            Quote("https://images.pexels.com/photos/235986/pexels-photo-235986.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Funny"),
            Quote("https://images.pexels.com/photos/235615/pexels-photo-235615.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Funny"),
            Quote("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Alone"),
            Quote("https://images.pexels.com/photos/2307562/pexels-photo-2307562.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Friendship"),
            Quote("https://images.pexels.com/photos/236214/pexels-photo-236214.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Can't you see that I'm only advising you to beg yourself not to be so dumb?","Age"),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.noti_search_menu,menu)
        return true
    }

    override fun onQuoteClick(quotes: ArrayList<Quote>, position: Int) {
        val intent = Intent(this,FeaturedQuotesActivity::class.java)
        intent.apply {
            putParcelableArrayListExtra(Constants.QUOTE_LIST_KEY,quotes)
            putExtra(Constants.QUOTE_POSITION_KEY,position)
        }
        startActivity(intent)
    }

    override fun onCategoryClick(category: Category, position: Int) {
        val intent = Intent(this,CategoryQuotesActivity::class.java)
        intent.putExtra(Constants.CATEGORY_KEY,category)
        intent.putExtra(Constants.QUOTES_TYPE_KEY,Constants.FROM_SECTION_TWO)
        startActivity(intent)
    }

    override fun onViewALlTvClick(quotes: ArrayList<Quote>, position: Int, sectionKey:String) {
        val intent = Intent(this,CategoryQuotesActivity::class.java)
        intent.putParcelableArrayListExtra(Constants.QUOTE_LIST_KEY,quotes)
        intent.putExtra(Constants.QUOTES_TYPE_KEY,sectionKey)
        startActivity(intent)
    }




}