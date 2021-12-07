package com.reddevx.instaquotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.reddevx.instaquotes.R
import com.reddevx.instaquotes.models.Category
import com.reddevx.instaquotes.ui.interfaces.QuoteInteraction
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(val categoryList:ArrayList<Category>,private val listener:QuoteInteraction,private val viewType:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SECTION_TWO_TYPE = 0
        const val NAV_CATEGORIES_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SECTION_TWO_TYPE)
        return SectionTwoCategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.section_two_categories_item,parent,false))
        else
            return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.categories_item,parent,false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SectionTwoCategoryViewHolder) {
            holder.apply {
                Glide.with(itemView)
                    .load(categoryList[position].categoryImage)
                    .into(holder.categoryImage)
                categoryName.text = categoryList[position].categoryName
            }
        } else {
            val cHolder = holder as CategoryViewHolder
            cHolder.apply {
                Glide.with(itemView)
                    .load(categoryList[position].categoryImage)
                    .into(cHolder.categoryImage)
                categoryName.text = categoryList[position].categoryName
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size
    override fun getItemViewType(position: Int): Int {
        if (viewType == SECTION_TWO_TYPE)
            return SECTION_TWO_TYPE
        return NAV_CATEGORIES_TYPE
    }

    inner class SectionTwoCategoryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {

        val categoryImage:CircleImageView
        val categoryName:TextView

        init {
            categoryImage = itemView.findViewById(R.id.section_two_categoryImage)
            categoryName = itemView.findViewById(R.id.categoryName)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                listener.onCategoryClick(categoryList[bindingAdapterPosition],bindingAdapterPosition)
        }

    }

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