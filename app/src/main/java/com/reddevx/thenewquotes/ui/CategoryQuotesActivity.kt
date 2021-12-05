package com.reddevx.thenewquotes.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.adapters.CategoryAdapter
import com.reddevx.thenewquotes.adapters.RecentQuotesAdapter
import com.reddevx.thenewquotes.database.DatabaseManager
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.interfaces.FavoriteListener
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction

class CategoryQuotesActivity : AppCompatActivity(), QuoteInteraction {


    private lateinit var quotesRv: RecyclerView
    private lateinit var quotesAdapter: RecentQuotesAdapter
    private lateinit var toolbarTv: TextView
    private lateinit var toolbarDelBtn:Button

    private val categoryQuotesList = ArrayList<Quote>()

    private val fireStore = FirebaseFirestore.getInstance()

    companion object{
        private lateinit var mListener:FavoriteListener
        fun setOnFavoriteClickListener(listener: FavoriteListener){
            mListener = listener
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_quotes)
        val toolbar = findViewById<Toolbar>(R.id.category_quote_toolbar)
        toolbarTv = toolbar.findViewById(R.id.category_quote_toolbar_tv)
        toolbarDelBtn = toolbar.findViewById(R.id.category_quote_toolbar_deleteAll_btn)

        quotesRv = findViewById(R.id.category_quote_recycler_view)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent != null) {
            prepareData()
        }
    }

    private fun loadAllQuotes(mCategory:String){
        val mFeaturedColl = fireStore.collection("quotes")
        val mRecentColl = fireStore.collection("recent")
        mFeaturedColl
            .whereEqualTo("category",mCategory)
            .get()
            .addOnSuccessListener(this) { snapShot ->
                for (doc in snapShot!!.documents) {
                    val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                    val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                    val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                    val quote = Quote(imageUrl, quoteText, category)
                    categoryQuotesList.add(quote)
                    quotesAdapter.notifyItemInserted(categoryQuotesList.size - 1)
                }
            }
            .addOnFailureListener(this
            ) { e -> Log.e("Listening failed!>", "onFailure: $e",) }
        mRecentColl
            .whereEqualTo("category",mCategory)
            .get()
            .addOnSuccessListener(this) { snapShot ->
                for (doc in snapShot!!.documents) {
                    val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                    val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                    val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                    val quote = Quote(imageUrl, quoteText, category)
                    categoryQuotesList.add(quote)
                    quotesAdapter.notifyItemInserted(categoryQuotesList.size - 1)
                }
            }
    }

    private fun prepareCategory(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_TWO)){
            val categoryName:String = intent.getStringExtra(MainActivity.Constants.CATEGORY_KEY)!!
            quotesAdapter = RecentQuotesAdapter(categoryQuotesList,this, mListener,context = this)
            toolbarTv.text = categoryName
            toolbarDelBtn.visibility = View.GONE
            loadAllQuotes(categoryName)
            buildRecyclerView(GridLayoutManager(this,2))
            return true
        }
        return false
    }


    private fun prepareData() {
        if (prepareFeaturedQuotes(intent))
            return
        if (prepareCategory(intent))
            return
        if (prepareRecentQuotes(intent))
            return
        if (prepareCategories(intent))
            return
        if (prepareFavorites(intent))
            return
    }

    private fun prepareFeaturedQuotes(intent:Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_ONE)) {
            val quoteList:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY)!!
            quotesAdapter = RecentQuotesAdapter(quoteList,this,context = this)
            toolbarTv.text = "Featured Quotes"
            toolbarDelBtn.visibility = View.GONE
            buildRecyclerView(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false))
            return true
        }
        return false
    }




    private fun prepareRecentQuotes(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_SECTION_THREE)){
            val recentQuotes:ArrayList<Quote> = intent.getParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY)!!
            quotesAdapter = RecentQuotesAdapter(recentQuotes,this, mListener,context = this)
            toolbarTv.text = "Recent Quotes"
            toolbarDelBtn.visibility = View.GONE
            buildRecyclerView(StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL))
            return true
        }
        return false
    }

    private fun prepareCategories(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_NAV_CATEGORIES)){
            val categories = ArrayList<Category>()
            val categoryAdapter = CategoryAdapter(categories,this,CategoryAdapter.NAV_CATEGORIES_TYPE)
            toolbarTv.text = "Categories"
            buildRecyclerView(GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false),categoryAdapter)
            loadCategories(categories,categoryAdapter)
            return true
        }
        return false
    }

    private fun loadCategories(categories:ArrayList<Category>, adapter: CategoryAdapter){
        val mCategoryColl = fireStore.collection("categories")
        mCategoryColl
            .orderBy("name",Query.Direction.ASCENDING)
            .addSnapshotListener(this) { snapShot, error ->
                if (error != null){
                    Toast.makeText(this, "Error :${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (doc in snapShot!!.documents){
                    val image = doc.getString("image")!!
                    val name = doc.getString("name")!!
                    val category = Category(image,name)
                    categories.add(category)
                    adapter.notifyItemInserted(categories.size - 1)
                }
            }
    }

    private fun prepareFavorites(intent: Intent) : Boolean {
        if (intent.getStringExtra(MainActivity.Constants.QUOTES_TYPE_KEY).equals(MainActivity.Constants.FROM_FAVORITES)) {
            val db = DatabaseManager.invoke(this)!!
            db.open()
            val favorites = db.getUserFavorites()
            db.close()
            quotesAdapter = RecentQuotesAdapter(favorites,listener = this,
                mListener,isFavorties = true,context = this)
            toolbarTv.text = "Favorites"
            toolbarDelBtn.visibility = View.VISIBLE
            toolbarDelBtn.setOnClickListener {
                showDialog()
            }
            val favoritesRv = buildRecyclerView(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false))
            favoritesRv.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
            return true
        }
        return false
    }

    private fun buildRecyclerView(rvLayoutManger:RecyclerView.LayoutManager, categoryAdapter:CategoryAdapter? = null) : RecyclerView {

        return quotesRv.apply {
            if (categoryAdapter == null)
                adapter = quotesAdapter
            else
                adapter = categoryAdapter
            layoutManager = rvLayoutManger
            hasFixedSize()

        }

    }

    override fun onQuoteClick(quotes: ArrayList<Quote>, position: Int) {
        Log.i("CategoryActivity", "onClick: OnClick called")
        val intent = Intent(this,FeaturedQuotesActivity::class.java)
        intent.apply {
            putParcelableArrayListExtra(MainActivity.Constants.QUOTE_LIST_KEY,quotes)
            putExtra(MainActivity.Constants.QUOTE_POSITION_KEY,position)
        }
        startActivity(intent)
    }

    override fun onCategoryClick(category: Category, position: Int) {
        val intent = Intent(this, CategoryQuotesActivity::class.java)
        intent.putExtra(MainActivity.Constants.CATEGORY_KEY, category.categoryName)
        intent.putExtra(MainActivity.Constants.QUOTES_TYPE_KEY, MainActivity.Constants.FROM_SECTION_TWO)
        startActivity(intent)
    }

    override fun onViewAllTvClick(quotes: ArrayList<Quote>, position: Int, sectionKey: String){
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }

    private fun showDialog(){
        val mDialog: AlertDialog
        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                        val db = DatabaseManager.invoke(this)!!
                        db.open()
                        db.deleteAll()
                        db.close()
                        quotesAdapter.removeAll()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("Favorites")
            .setMessage("Do you want to delete all favorite quotes?")
            .apply {
                setPositiveButton("YES",dialogInterface)
                setNegativeButton("NO",dialogInterface)
            }
        mDialog = builder.create()
        mDialog.show()

    }



}