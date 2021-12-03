package com.reddevx.thenewquotes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.reddevx.thenewquotes.ui.MainActivity
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import java.lang.Exception

import kotlin.collections.ArrayList

class MainAdapter(
    val categoryList:ArrayList<Category>,
    val mContext: MainActivity,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val QUOTE_VIEW_TYPE:Int = 0
    private val CATEGORY_VIEW_TYPE:Int = 1
    private val RECENT_QUOTE_VIEW_TYPE:Int = 2

    private val quotes = ArrayList<Quote>()
    private val recentQuotes = ArrayList<Quote>()

    private lateinit var quotesAdapter:QuotesAdapter
    private lateinit var categoryAdapter:CategoryAdapter
    private lateinit var recentQuotesAdapter:RecentQuotesAdapter
    private val fireStore = FirebaseFirestore.getInstance()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View
        if (viewType == QUOTE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.section_one,parent,false)
            return SectionOneViewHolder(view)
        }else if (viewType == CATEGORY_VIEW_TYPE ){
            view = layoutInflater.inflate(R.layout.section_two,parent,false)
            return SectionTwoViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.section_three,parent,false)
            return SectionThreeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SectionOneViewHolder){
            // section one (Featured Quotes)


            val sectionOneViewHolder:SectionOneViewHolder = holder
            quotesAdapter = QuotesAdapter(quotes,mContext)
            sectionOneViewHolder.typeQuotesTv.text = "Featured Quotes"
            sectionOneViewHolder.viewAllTv.text = "VIEW ALL"
            sectionOneViewHolder.childRecyclerView.apply {
                adapter = quotesAdapter
                layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                hasFixedSize()
            }
            loadFeaturedQuotes()



            PagerSnapHelper().attachToRecyclerView(sectionOneViewHolder.childRecyclerView)

        }else if (holder is SectionTwoViewHolder){
            // section two (Categories)
            val sectionTwoViewHolder:SectionTwoViewHolder = holder
            categoryAdapter = CategoryAdapter(categoryList,mContext,CategoryAdapter.SECTION_TWO_TYPE)
            sectionTwoViewHolder.childRecyclerView.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                hasFixedSize()
            }


        } else {
            // section three (Recent Quotes)
            val sectionThreeViewHolder:SectionThreeViewHolder = holder as SectionThreeViewHolder
            recentQuotesAdapter = RecentQuotesAdapter(recentQuotes,mContext,context = mContext)
            sectionThreeViewHolder.apply {
                recentQuotesTv.text = "Recent Quotes"
                recentViewALlTv.text = "VIEW ALL"
                childRecyclerView.apply {
                    adapter = recentQuotesAdapter
                    layoutManager = GridLayoutManager(mContext,2)
                    hasFixedSize()
                }
            }

            loadRecentQuotes()


        }

    }

    fun notifyChanges(){
        recentQuotesAdapter.notifyItemRangeChanged(0,10)
    }

    private fun loadFeaturedQuotes(){
        val mFeaturedColl = fireStore.collection("quotes")
        mFeaturedColl
            .orderBy("date",Query.Direction.DESCENDING)
            .limit(14)
            .get()
            .addOnSuccessListener(mContext, object : OnSuccessListener<QuerySnapshot> {
                override fun onSuccess(snapShot: QuerySnapshot?) {
                    for (doc in snapShot!!.documents){
                        val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                        val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                        val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                        val quote = Quote(imageUrl,quoteText,category)
                        quotes.add(quote)
                        quotesAdapter.notifyItemInserted(quotes.size-1)
                    }
                }
            })
            .addOnFailureListener(mContext, object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Log.e("GGGGGGG", "onFailure: $e", )
                }

            })
    }


    private fun loadRecentQuotes(){
        val mRecentColl = fireStore.collection("recent")
        mRecentColl
            .orderBy("date",Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener(mContext, object : EventListener<QuerySnapshot> {
            override fun onEvent(snapShot: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.w("Listen failed",error.toString())
                    return
                }
                for (doc in snapShot!!.documents){
                    val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
                    val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
                    val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                    val quote = Quote(imageUrl,quoteText,category)
                    recentQuotes.add(quote)
                    recentQuotesAdapter.notifyItemInserted(recentQuotes.size - 1)
                }
            }
        })
    }


    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
            return QUOTE_VIEW_TYPE
        }else if (position == 1) {
            return CATEGORY_VIEW_TYPE
        }
        return RECENT_QUOTE_VIEW_TYPE
    }

    inner class SectionOneViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val typeQuotesTv:TextView
        val viewAllTv:TextView
        val childRecyclerView:RecyclerView

        init {
            typeQuotesTv = itemView.findViewById(R.id.featured_quotes_tv)
            viewAllTv = itemView.findViewById(R.id.featured_view_all)
            childRecyclerView = itemView.findViewById(R.id.featured_quotes_rv)
            viewAllTv.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                mContext.onViewAllTvClick(quotes,bindingAdapterPosition,MainActivity.Constants.FROM_SECTION_ONE)
        }
    }

    inner class SectionTwoViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

        val childRecyclerView:RecyclerView

        init {
            childRecyclerView = itemView.findViewById(R.id.categories_rv)
        }
    }

    inner class SectionThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val recentQuotesTv:TextView
        val recentViewALlTv:TextView
        val childRecyclerView:RecyclerView
        init {
            recentQuotesTv = itemView.findViewById(R.id.recent_quotes_tv)
            recentViewALlTv = itemView.findViewById(R.id.recent_view_all)
            childRecyclerView = itemView.findViewById(R.id.recent_quote_rv)
            recentViewALlTv.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION){
                mContext.onViewAllTvClick(recentQuotes,bindingAdapterPosition,MainActivity.Constants.FROM_SECTION_THREE)
                }
        }
    }




}