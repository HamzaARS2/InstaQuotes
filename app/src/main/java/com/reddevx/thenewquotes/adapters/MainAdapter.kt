package com.reddevx.thenewquotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.thenewquotes.ui.MainActivity
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.models.Quote
import kotlin.collections.ArrayList

class MainAdapter(val quoteList:ArrayList<Quote>,
                  val categoryList:ArrayList<Category>,
                  val recentQuoteList: ArrayList<Quote>,
                  val mContext: MainActivity,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val QUOTE_VIEW_TYPE:Int = 0
    private val CATEGORY_VIEW_TYPE:Int = 1
    private val RECENT_QUOTE_VIEW_TYPE:Int = 2

    private lateinit var quotesAdapter:QuotesAdapter
    private lateinit var categoryAdapter:CategoryAdapter
    private lateinit var recentQuotesAdapter:RecentQuotesAdapter


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
            quotesAdapter = QuotesAdapter(quoteList,mContext)
            sectionOneViewHolder.typeQuotesTv.text = "Featured Quotes"
            sectionOneViewHolder.viewAllTv.text = "VIEW ALL"
            sectionOneViewHolder.childRecyclerView.apply {
                adapter = quotesAdapter
                layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                hasFixedSize()
            }
            PagerSnapHelper().attachToRecyclerView(sectionOneViewHolder.childRecyclerView)

        }else if (holder is SectionTwoViewHolder){
            // section two (Categories)
            val sectionTwoViewHolder:SectionTwoViewHolder = holder
            categoryAdapter = CategoryAdapter(categoryList,mContext)
            sectionTwoViewHolder.childRecyclerView.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
                hasFixedSize()
            }


        } else {
            // section three (Recent Quotes)
            val sectionThreeViewHolder:SectionThreeViewHolder = holder as SectionThreeViewHolder
            recentQuotesAdapter = RecentQuotesAdapter(recentQuoteList,mContext)
            sectionThreeViewHolder.apply {
                recentQuotesTv.text = "Recent Quotes"
                recentViewALlTv.text = "VIEW ALL"
                childRecyclerView.apply {
                    adapter = recentQuotesAdapter
                    layoutManager = GridLayoutManager(mContext,2)
                    hasFixedSize()
                }
            }


        }

    }

     fun refreshData(){
        quotesAdapter.notifyDataSetChanged()
        recentQuotesAdapter.notifyDataSetChanged()
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
            if (adapterPosition != RecyclerView.NO_POSITION)
                mContext.onViewAllTvClick(quoteList,adapterPosition,MainActivity.Constants.FROM_SECTION_ONE)
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
            if (adapterPosition != RecyclerView.NO_POSITION){
                mContext.onViewAllTvClick(recentQuoteList,adapterPosition,MainActivity.Constants.FROM_SECTION_THREE)
                }
        }
    }




}