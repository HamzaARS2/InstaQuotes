package com.reddevx.instaquotes.ui

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.reddevx.instaquotes.R

class SettingsActivity : AppCompatActivity() , View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {



    private lateinit var notificationLayout:ConstraintLayout
    private lateinit var checkBox: CheckBox
    private lateinit var nightModeSwitch: SwitchCompat
    private lateinit var settingsToolbar: androidx.appcompat.widget.Toolbar

    private lateinit var sp:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor

    companion object {
        var NIGHT_MODE = false
        const val NIGHT_MODE_STATE = "dark_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsToolbar = findViewById(R.id.settings_toolbar)
        settingsToolbar.title = ""
        setSupportActionBar(settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        notificationLayout = findViewById(R.id.settings_notification)
        checkBox = findViewById(R.id.notification_checkbox)
        nightModeSwitch = findViewById(R.id.nightmode_switch)
        notificationLayout.setOnClickListener(this)

        sp = getSharedPreferences(NIGHT_MODE_STATE, MODE_PRIVATE)
        editor = sp.edit()
        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> NIGHT_MODE = true
            Configuration.UI_MODE_NIGHT_NO -> NIGHT_MODE = false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> NIGHT_MODE = false
        }
        nightModeSwitch.setOnCheckedChangeListener(this)
        nightModeSwitch.isChecked = NIGHT_MODE
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

    override fun onCheckedChanged(nightSwitch: CompoundButton?, isChecked: Boolean) {
       nightModeChecker(isChecked)
    }

    private fun nightModeChecker(check:Boolean){
        if (check){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            NIGHT_MODE = true
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NIGHT_MODE = false
        }
    }

    override fun onStop() {
        super.onStop()
        editor.putBoolean(NIGHT_MODE_STATE, NIGHT_MODE)
        editor.apply()
    }


}