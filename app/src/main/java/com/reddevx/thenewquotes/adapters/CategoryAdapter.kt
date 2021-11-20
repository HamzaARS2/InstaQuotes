package com.reddevx.thenewquotes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.models.Category
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(val categoryList:ArrayList<Category>, val context:Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.categorie_item,parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.apply {
            Glide.with(context).load(categoryList[position].categoryImage).into(holder.categoryImage)
            categoryName.text = categoryList[position].categoryName
        }
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage:CircleImageView
        val categoryName:TextView

        init {
            categoryImage = itemView.findViewById(R.id.categoryImage)
            categoryName = itemView.findViewById(R.id.categoryName)
        }
    }

}