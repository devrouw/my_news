package com.lollipop.mynews.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.mynews.R
import com.lollipop.mynews.databinding.RowSourceBinding
import com.lollipop.mynews.service.model.Category
import com.lollipop.mynews.service.model.Source
import com.squareup.picasso.Picasso
import java.util.*

class AdapterSourceList(context: Context) : RecyclerView.Adapter<AdapterSourceList.ViewHolder>() {
    private var items : MutableList<Source> = ArrayList()
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var lastPosition = -1
    private val ctx: Context

    init {
        ctx = context
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data : List<Source>){
        items.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        items.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_source, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(binding){
                val c: Source = items[position]
                name.text = c.name
                label.text = c.name?.first().toString()
                setAnimation(holder.itemView, position)
                lytParent.setOnClickListener {
                    onItemClickCallback.onClick(c)
                }
            }
        }
    }


    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RowSourceBinding.bind(view)
    }

    interface OnItemClickCallback {
        fun onClick(source: Source)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}