package com.lollipop.mynews.view.adapter

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
import com.lollipop.mynews.service.model.Category
import com.squareup.picasso.Picasso
import java.util.*

class AdapterCategoryList(context: Context, items: List<Category>) :
    RecyclerView.Adapter<AdapterCategoryList.ViewHolder>() {
    private var items: List<Category> = ArrayList<Category>()
    private val ctx: Context
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: Category?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var name: TextView
        var image: ImageView
        var lyt_parent: LinearLayout

        init {
            name = v.findViewById<View>(R.id.name) as TextView
            image = v.findViewById<View>(R.id.image) as ImageView
            lyt_parent = v.findViewById<View>(R.id.lyt_parent) as LinearLayout
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_category, parent, false)

        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c: Category = items[position]
        holder.name.setText(c.name)
        Picasso.with(ctx).load(c.icon).into(holder.image)
        (holder.image.background as GradientDrawable).setColor(Color.parseColor(c.color))
        setAnimation(holder.itemView, position)
        holder.lyt_parent.setOnClickListener { view ->
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClick(view, c, position)
            }
        }
    }

    // Here is the key method to apply the animation
    private var lastPosition = -1

    // Provide a suitable constructor (depends on the kind of dataset)
    init {
        this.items = items
        ctx = context
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items.size
    }
}