package com.reddevx.instaquotes.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.firestore.*
import com.reddevx.instaquotes.AdManager
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.adapters.MainAdapter
import com.reddevx.instaquotes.models.Category
import com.reddevx.instaquotes.models.Quote
import com.reddevx.instaquotes.ui.interfaces.FavoriteListener
import com.reddevx.instaquotes.ui.interfaces.QuoteInteraction
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), QuoteInteraction,
    NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener,
    FavoriteListener, MainAdapter.ProgressBarListener {


    object Constants {
        const val QUOTE_LIST_KEY = "quote_list"
        const val QUOTE_POSITION_KEY = "quote_position"
        const val CATEGORY_KEY = "category_quotes_key"
        const val QUOTES_TYPE_KEY = " which_list_will_display"

        const val FROM_SECTION_ONE = "1"
        const val FROM_SECTION_TWO = "2"
        const val FROM_SECTION_THREE = "3"
        const val FROM_NAV_CATEGORIES = "4"
        const val FROM_FAVORITES = "5"

        const val FIRE_STORE_IMAGE_KEY = "image"
        const val FIRE_STORE_QUOTE_KEY = "quote"
        const val FIRE_STORE_CATEGORY_KEY = "category"

        const val UPDATE_REQ_CODE = 1001
    }

    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var mainProgessBar: ProgressBar

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var refreshLayout: SwipeRefreshLayout
    private var doubleBackPressToExit = true

    private lateinit var adManager:AdManager



    private lateinit var appUpdateManager: AppUpdateManager


    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.main_drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        refreshLayout = findViewById(R.id.swipe_refresh_layout)
        mainRecyclerView = findViewById(R.id.main_recycler_view)
        mainProgessBar = findViewById(R.id.main_progress_bar)


        adManager = AdManager(this)
        navigationView.setNavigationItemSelectedListener(this)
        refreshLayout.setOnRefreshListener(this)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appUpdateManager = AppUpdateManagerFactory.create(this)


        mainAdapter = MainAdapter(
            mContext = this@MainActivity
        )
        mainRecyclerView.apply {
            adapter = mainAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()

        }
        checkForUpdates()
        adManager.prepareAdCounter()


    }

    override fun onResume() {
        super.onResume()

        // Prepare interstitial ad
        adManager.loadAd()
        FeaturedQuotesActivity.setOnFavoriteClickListener(this)
        CategoryQuotesActivity.setOnFavoriteClickListener(this)
        SearchActivity.setOnFavoriteSearchClickListener(this)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    showSnackInstaller()
                }
            }
    }


    override fun onBackPressed() {
        if (!doubleBackPressToExit) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        doubleBackPressToExit = false
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackPressToExit = true }, 2000)
    }


    override fun onStop() {
        if (appUpdateManager != null){appUpdateManager.unregisterListener(installStateListener())}
        super.onStop()
        adManager.saveCounterValue()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.notification_item -> {
                val intent = Intent(this, NotificationsActivity::class.java)
                startActivity(intent)
            }
            R.id.search_item -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.noti_search_menu, menu)
        return true
    }

    override fun onQuoteClick(quotes: ArrayList<Quote>, position: Int) {
        val intent = Intent(this, FeaturedQuotesActivity::class.java)
        intent.apply {
            putParcelableArrayListExtra(Constants.QUOTE_LIST_KEY, quotes)
            putExtra(Constants.QUOTE_POSITION_KEY, position)
        }
        startActivity(intent)
    }

    override fun onCategoryClick(category: Category, position: Int) {
        val intent = Intent(this, CategoryQuotesActivity::class.java)
        intent.putExtra(Constants.CATEGORY_KEY, category.categoryName)
        intent.putExtra(Constants.QUOTES_TYPE_KEY, Constants.FROM_SECTION_TWO)
        startActivity(intent)
    }

    override fun onViewAllTvClick(quotes: ArrayList<Quote>, position: Int, sectionKey: String) {
        val intent = Intent(this, CategoryQuotesActivity::class.java)
        intent.putParcelableArrayListExtra(Constants.QUOTE_LIST_KEY, quotes)
        intent.putExtra(Constants.QUOTES_TYPE_KEY, sectionKey)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home_menu_item -> {
                finish()
                val newIntent = intent
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(newIntent)
                drawerLayout.closeDrawer(GravityCompat.START)

            }
            R.id.nav_categories_menu_item -> {
                val intent = Intent(this, CategoryQuotesActivity::class.java)
                intent.putExtra(Constants.QUOTES_TYPE_KEY, Constants.FROM_NAV_CATEGORIES)
                startActivity(intent)
            }
            R.id.nav_favorites_menu_item -> {
                val intent = Intent(this, CategoryQuotesActivity::class.java)
                intent.putExtra(Constants.QUOTES_TYPE_KEY, Constants.FROM_FAVORITES)
                drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(intent)
            }
            R.id.nav_settings_menu_item -> {
                drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_share_menu_item -> {
                val intent = Intent(Intent.ACTION_SEND)

                intent.apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        " Install App from:\n https://play.google.com/store/apps/details?id=$packageName"
                    )
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent.createChooser(intent, "Install App from:"))


            }
            R.id.nav_rate_menu_item -> {
                val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_exit_menu_item -> showDialog()

        }

        return true
    }

    private fun showDialog() {
        val mDialog: AlertDialog

        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> finishAffinity()
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to close the app?")
            .apply {
                setPositiveButton("YES", dialogInterface)
                setNegativeButton("NO", dialogInterface)
            }
        mDialog = builder.create()
        mDialog.show()

    }

    override fun onRefresh() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0);
        refreshLayout.isRefreshing = false
    }

    override fun onFavoriteClick() {
        mainAdapter.notifyChanges()
    }

    override fun onFavoriteRemoved(position: Int) {
    }

    override fun onProgressFinished() {
        mainProgessBar.visibility = View.GONE
        mainRecyclerView.visibility = View.VISIBLE
    }

    private fun checkForUpdates() {
        appUpdateManager.registerListener(installStateListener())
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        Constants.UPDATE_REQ_CODE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
            .addOnFailureListener {
                Log.e("Update failed", "Update failed${it.message.toString()}")
            }
    }

    private fun showSnackInstaller() {
        Snackbar.make(findViewById(android.R.id.content), "Update downloaded", LENGTH_INDEFINITE)
            .setAction("Install") { appUpdateManager.completeUpdate() }
            .show()
    }



    private fun installStateListener() : InstallStateUpdatedListener  = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                showSnackInstaller()
            }
        }


    }



