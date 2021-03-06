package com.reddevx.instaquotes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.firestore.*
import com.reddevx.instaquotes.ui.MainActivity
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.models.Category
import com.reddevx.instaquotes.models.Quote

import kotlin.collections.ArrayList

class MainAdapter(
    val mContext: MainActivity,
    private val progressListener: ProgressBarListener = mContext
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val QUOTE_VIEW_TYPE: Int = 0
    private val CATEGORY_VIEW_TYPE: Int = 1
    private val AD_VIEW_TYPE: Int = 2
    private val RECENT_QUOTE_VIEW_TYPE: Int = 3


    private val quotes = ArrayList<Quote>()
    private val recentQuotes = ArrayList<Quote>()
    private val categoryList = ArrayList<Category>()

    private lateinit var quotesAdapter: QuotesAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recentQuotesAdapter: RecentQuotesAdapter
    private val fireStore = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == QUOTE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.section_one, parent, false)
            return SectionOneViewHolder(view)
        } else if (viewType == CATEGORY_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.section_two, parent, false)
            return SectionTwoViewHolder(view)
        } else if (viewType == AD_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.ad_section_layout, parent, false)
            return AdViewSectionViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.section_three, parent, false)
            return SectionThreeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SectionOneViewHolder) {
            // section one (Featured Quotes)
            val sectionOneViewHolder: SectionOneViewHolder = holder
            quotesAdapter = QuotesAdapter(quotes, mContext)
            sectionOneViewHolder.typeQuotesTv.text = "Featured Quotes"
            sectionOneViewHolder.viewAllTv.text = "VIEW ALL"
            sectionOneViewHolder.childRecyclerView.apply {
                adapter = quotesAdapter
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                hasFixedSize()
            }


        } else if (holder is SectionTwoViewHolder) {
            // section two (Categories)
            val sectionTwoViewHolder: SectionTwoViewHolder = holder
            categoryAdapter =
                CategoryAdapter(categoryList, mContext, CategoryAdapter.SECTION_TWO_TYPE)
            sectionTwoViewHolder.childRecyclerView.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                hasFixedSize()
            }



        } else if (holder is AdViewSectionViewHolder) {
            // AdView (Banner)
            val adViewHolder = holder
            val adRequest = AdRequest.Builder().build()
            adViewHolder.adView.loadAd(adRequest)
        } else {
            // section three (Recent Quotes)
            val sectionThreeViewHolder: SectionThreeViewHolder = holder as SectionThreeViewHolder
            recentQuotesAdapter = RecentQuotesAdapter(recentQuotes, mContext, context = mContext)
            sectionThreeViewHolder.apply {
                recentQuotesTv.text = "Recent Quotes"
                recentViewALlTv.text = "VIEW ALL"
                childRecyclerView.apply {
                    adapter = recentQuotesAdapter
                    layoutManager = GridLayoutManager(mContext, 2)
                    hasFixedSize()
                }
            }

        }
    }


    override fun getItemCount(): Int = 4

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            QUOTE_VIEW_TYPE
        } else if (position == 1) {
            CATEGORY_VIEW_TYPE
        } else if (position == 2)
            AD_VIEW_TYPE
        else
            RECENT_QUOTE_VIEW_TYPE
    }

    inner class SectionOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val typeQuotesTv: TextView
        val viewAllTv: TextView
        val childRecyclerView: RecyclerView

        init {
            typeQuotesTv = itemView.findViewById(R.id.featured_quotes_tv)
            viewAllTv = itemView.findViewById(R.id.featured_view_all)
            childRecyclerView = itemView.findViewById(R.id.featured_quotes_rv)
            viewAllTv.setOnClickListener(this)
            PagerSnapHelper().attachToRecyclerView(childRecyclerView)
            loadFeaturedQuotes()
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                mContext.onViewAllTvClick(
                    quotes,
                    bindingAdapterPosition,
                    MainActivity.Constants.FROM_SECTION_ONE
                )
        }
    }

    inner class SectionTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val childRecyclerView: RecyclerView

        init {
            childRecyclerView = itemView.findViewById(R.id.categories_rv)
            loadCategories()
        }
    }

    inner class AdViewSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adView: AdView

        init {
            adView = itemView.findViewById(R.id.mainAdView1)
        }
    }

    inner class SectionThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val recentQuotesTv: TextView
        val recentViewALlTv: TextView
        val childRecyclerView: RecyclerView

        init {
            recentQuotesTv = itemView.findViewById(R.id.recent_quotes_tv)
            recentViewALlTv = itemView.findViewById(R.id.recent_view_all)
            childRecyclerView = itemView.findViewById(R.id.recent_quote_rv)
            recentViewALlTv.setOnClickListener(this)
            loadRecentQuotes()
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                mContext.onViewAllTvClick(
                    recentQuotes,
                    bindingAdapterPosition,
                    MainActivity.Constants.FROM_SECTION_THREE
                )
            }
        }
    }

    fun notifyChanges() {
        recentQuotesAdapter.notifyItemRangeChanged(0, 10)
    }

    private fun loadFeaturedQuotes() {
        val mFeaturedColl = fireStore.collection("quotes")
        mFeaturedColl
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(14)
            .get()
            .addOnCompleteListener(mContext) { task ->
                if (task.isSuccessful) {
                    progressListener.onProgressFinished()
                    for (doc in task.result.documents) {
                        val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                        val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                        val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                        val quote = Quote(imageUrl, quoteText, category)
                        quotes.add(quote)
                        quotesAdapter.notifyItemInserted(quotes.size - 1)
                    }
                } else {
                    Log.e("Failed!", task.exception?.message.toString())
                }


            }


    }

    private fun loadCategories() {
        val mCategoryColl = fireStore.collection("categories")
        mCategoryColl
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener(mContext) { task ->
                if (task.isSuccessful) {
                    for (doc in task.result.documents) {
                        val image = doc.getString("image")!!
                        val name = doc.getString("name")!!
                        val category = Category(image, name)
                        categoryList.add(category)
                        categoryAdapter.notifyItemInserted(categoryList.size - 1)
                    }
                } else
                    Log.e("Retrieve data failed!", task.exception?.message.toString())
            }


    }


    private fun loadRecentQuotes() {

        val mRecentColl = fireStore.collection("recent")
        mRecentColl
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnCompleteListener(mContext) { task ->
                if (task.isSuccessful) {
                    for (doc in task.result.documents) {
                        val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                        val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                        val category =
                            doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                        val quote = Quote(imageUrl, quoteText, category)
                        recentQuotes.add(quote)
                        recentQuotesAdapter.notifyItemInserted(recentQuotes.size - 1)
                    }
                } else {
                    Log.e("Retrieve data failed!", task.exception?.message.toString())
                }
            }
    }

    interface ProgressBarListener {
        fun onProgressFinished()
    }


}