package com.reddevx.thenewquotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.thenewquotes.R

class SearchActivity : AppCompatActivity() {
    private lateinit var searchToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var searchRv:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchToolbar = findViewById(R.id.search_toolbar)
        searchRv = findViewById(R.id.searchRv)
        searchToolbar.title = ""
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.search_quotes_menu,menu)

        return true
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
}