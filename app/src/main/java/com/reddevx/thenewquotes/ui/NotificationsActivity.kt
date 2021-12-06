package com.reddevx.thenewquotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.thenewquotes.R

class NotificationsActivity : AppCompatActivity() {
    private lateinit var notificationsRv:RecyclerView
    private lateinit var notificationsToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var noDataLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        notificationsRv = findViewById(R.id.notificationsRv)
        notificationsToolbar = findViewById(R.id.notifications_toolbar)
        noDataLayout = findViewById(R.id.nodata_LL)
        notificationsToolbar.title = ""
        setSupportActionBar(notificationsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        noDataLayout.visibility = View.VISIBLE
//        if (intent == null){
//            // No data
//
//        }else {
//            // There is data
//            noDataLayout.visibility = View.GONE
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }
}