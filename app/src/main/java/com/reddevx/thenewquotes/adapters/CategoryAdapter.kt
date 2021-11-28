package com.reddevx.thenewquotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Category
import com.reddevx.thenewquotes.ui.interfaces.QuoteInteraction
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(val categoryList:ArrayList<Category>,private val listener:QuoteInteraction) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.categorie_item,parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.apply {
            Glide.with(itemView).load(categoryList[position].categoryImage)
                .into(holder.categoryImage)
            categoryName.text = categoryList[position].categoryName
        }
    }

    override fun getItemCount(): Int = categoryList.size

    inner class CategoryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {


        val categoryImage:CircleImageView
        val categoryName:TextView

        init {
            categoryImage = itemView.findViewById(R.id.categoryImage)
            categoryName = itemView.findViewById(R.id.categoryName)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                listener.onCategoryClick(categoryList[bindingAdapterPosition],bindingAdapterPosition)
        }

    }



}