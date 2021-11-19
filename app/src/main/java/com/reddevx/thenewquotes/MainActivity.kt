package com.reddevx.thenewquotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.reddevx.thenewquotes.adapters.QuotesAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesRv: RecyclerView
    private lateinit var quotesAdapter: QuotesAdapter
    //private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        recyclerView = findViewById(R.id.featured_quotes_rv)
        categoriesRv = findViewById(R.id.categories_rv)
        quotesAdapter = QuotesAdapter(getQuotes())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter = quotesAdapter

        }

        categoriesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter = quotesAdapter
        }
    }
    private fun getQuotes() : ArrayList<String> {
        return arrayListOf("When brains were passed out, everyone was pleased with his brains; but when fortunes were given out, no one was satisfied with his fortune."
            ,"Were it not for differences of opinion, goods would go unsold."
            ,"The rooster dies with his eye still on the dunghill","The rooster dies with his eye still on the dunghill","The rooster dies with his eye still on the dunghill"
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.noti_search_menu,menu)
        return true
    }
}