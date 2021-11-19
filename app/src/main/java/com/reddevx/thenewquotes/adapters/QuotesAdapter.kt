package com.reddevx.thenewquotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reddevx.thenewquotes.R

class QuotesAdapter(val data:ArrayList<String>) : RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.latest_quote_item,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.quote.text = data[position]
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quote:TextView

        init {
            quote = itemView.findViewById(R.id.quote_tv)
        }

    }

}