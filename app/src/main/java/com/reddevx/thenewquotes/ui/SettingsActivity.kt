package com.reddevx.thenewquotes.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.reddevx.thenewquotes.R

class SettingsActivity : AppCompatActivity() , View.OnClickListener{



    private lateinit var notificationLayout:ConstraintLayout
    private lateinit var checkBox: CheckBox
    private lateinit var settingsToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsToolbar = findViewById(R.id.settings_toolbar)
        settingsToolbar.title = ""
        setSupportActionBar(settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        notificationLayout = findViewById(R.id.settings_notification)
        checkBox = findViewById(R.id.notification_checkbox)
        notificationLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        checkBox.apply {
            isChecked = !isChecked
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
           onBackPressed()
            return true
        }
        return true
    }



}