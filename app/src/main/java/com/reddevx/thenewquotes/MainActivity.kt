package com.reddevx.thenewquotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.reddevx.thenewquotes.adapters.CategoryAdapter
import com.reddevx.thenewquotes.adapters.MainAdapter
import com.reddevx.thenewquotes.adapters.QuotesAdapter
import com.reddevx.thenewquotes.adapters.RecentQuotesAdapter
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesRv: RecyclerView
    private lateinit var recentQuotesRv: RecyclerView
    private lateinit var quotesAdapter: QuotesAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recentQuotesAdapter: RecentQuotesAdapter

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

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainRecyclerView = findViewById(R.id.main_recycler_view)
        mainAdapter = MainAdapter(getQuotes(),getCategories(),getRecentQuotes(),this)
        mainRecyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
        }




//        recyclerView = findViewById(R.id.featured_quotes_rv)
//        quotesAdapter = QuotesAdapter(getQuotes())
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
//            adapter = quotesAdapter
//        }
//
//        val snapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
//
//        categoriesRv = findViewById(R.id.categories_rv)
//        categoryAdapter = CategoryAdapter(getCategories(),this)
//        categoriesRv.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
//            adapter = categoryAdapter
//        }
//
//        recentQuotesRv = findViewById(R.id.recent_quote_tv)
//        recentQuotesAdapter = RecentQuotesAdapter(getRecentQuotes(),this)
//        recentQuotesRv.apply {
//            layoutManager = GridLayoutManager(this@MainActivity,2)
//            adapter = recentQuotesAdapter
//        }
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
            Category(R.drawable.age_circle,"Age"),
            Category(R.drawable.alone_circle,"Alone"),
            Category(R.drawable.angry_circle,"Angry"),
            Category(R.drawable.family_circle,"Family"),
            Category(R.drawable.friendship_circle,"Friendship"),
            Category(R.drawable.funny_circle,"Funny"),
            Category(R.drawable.life_circle,"Life")
        )
    }

    private fun getRecentQuotes() :ArrayList<Quote> {
        return arrayListOf(
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false),
            Quote("https://images.pexels.com/photos/1108572/pexels-photo-1108572.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune.","Age",false)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.noti_search_menu,menu)
        return true
    }
}