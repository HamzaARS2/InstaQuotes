package com.reddevx.thenewquotes.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.reddevx.thenewquotes.QuoteLoader
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.MainAdapter
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity(), QuoteInteraction,
    NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    object Constants {
        const val QUOTE_LIST_KEY = "quote_list"
        const val QUOTE_POSITION_KEY = "quote_position"
        const val CATEGORIES_KEY = "categories_491"
        const val CATEGORY_KEY = "category_quotes_key"
        const val QUOTES_TYPE_KEY = " which_list_will_display"
        const val FAVORITES_KEY = "list_of_favorites"

        const val FROM_SECTION_ONE = "1"
        const val FROM_SECTION_TWO = "2"
        const val FROM_SECTION_THREE = "3"
        const val FROM_NAV_CATEGORIES = "4"
        const val FROM_FAVORITES = "5"

        const val FIRE_STORE_IMAGE_KEY = "image"
        const val FIRE_STORE_QUOTE_KEY = "quote"
        const val FIRE_STORE_CATEGORY_KEY = "category"
    }
    private var doubleBackPressToExit = true

    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainRecyclerView: RecyclerView

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var refreshLayout: SwipeRefreshLayout

    private val fAuth:FirebaseAuth = FirebaseAuth.getInstance()





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


        navigationView.setNavigationItemSelectedListener(this)
        refreshLayout.setOnRefreshListener(this)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mainAdapter = MainAdapter(
            categoryList = getCategories(),
            mContext = this@MainActivity
        )
        mainRecyclerView.apply {
            adapter = mainAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()

        }


    }

    override fun onStart() {
        super.onStart()
        if (fAuth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        else
        Toast.makeText(this, fAuth.currentUser?.email, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (!doubleBackPressToExit){
            super.onBackPressed()
            finishAffinity()
            return
        }

        doubleBackPressToExit = false
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackPressToExit = true }, 2000)
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

    private fun getQuotes(): ArrayList<Quote> {
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

    private fun getCategories(): ArrayList<Category> {
        return arrayListOf(
            Category(R.drawable.age_circle, "Age", getQuotes()),
            Category(R.drawable.alone_circle, "Alone", getQuotes()),
            Category(R.drawable.angry_circle, "Angry", getQuotes()),
            Category(R.drawable.family_circle, "Family", getQuotes()),
            Category(R.drawable.friendship_circle, "Friendship", getQuotes()),
            Category(R.drawable.patience_circle, "Patience", getQuotes()),
            Category(R.drawable.life_circle, "Life", getQuotes())
        )
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
                intent.putParcelableArrayListExtra(Constants.CATEGORIES_KEY, getCategories())
                intent.putExtra(Constants.QUOTES_TYPE_KEY, Constants.FROM_NAV_CATEGORIES)
                startActivity(intent)
            }
            R.id.nav_favorites_menu_item -> {
                val intent = Intent(this, CategoryQuotesActivity::class.java)
                intent.putParcelableArrayListExtra(Constants.FAVORITES_KEY, getQuotes())
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
                        " Install App from:\n https://play.google.com/store/apps/details?id=com.rm.instaquotes"
                    )
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent.createChooser(intent, "Install App from:"))
            }
            R.id.nav_log_out_menu_item -> {
                fAuth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.nav_exit_menu_item -> {
                showDialog()
            }

        }

        return true
    }

    private fun showDialog(){
        val dialog:AlertDialog

        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> finishAffinity()
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to close the app?")
            .apply {
                setPositiveButton("YES",dialogInterface)
                setNegativeButton("NO",dialogInterface)
            }
        dialog = builder.create()
        dialog.show()

    }

    override fun onRefresh() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0);
        refreshLayout.isRefreshing = false
    }


}