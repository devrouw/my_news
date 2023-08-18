package com.lollipop.mynews.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.mynews.R
import com.lollipop.mynews.helper.DateFormatLocale
import com.lollipop.mynews.service.model.Article
import com.lollipop.mynews.service.model.Source
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.util.*

class AdapterArticleList(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var items : MutableList<Article?> = ArrayList()
    private var originalItems: MutableList<Article?> = ArrayList()
    private val ctx: Context
    private lateinit var header: Article
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: Article?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        items.clear()
        originalItems.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data : List<Article>){
        items.addAll(data)
        originalItems.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setHeader(data: Article){
        this.header = data
        notifyDataSetChanged()
    }

    fun showLoadingIndicator() {
        // Add null item to represent the loading indicator
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun hideLoadingIndicator() {
        if (items.isNotEmpty() && items.last() == null) {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

    fun filter(query: String) {
        items.clear()

        if (query.isEmpty()) {
            items.addAll(originalItems)
        } else {
            val lowerCaseQuery = query.lowercase(Locale.ROOT)
            for (article in originalItems) {
                Timber.d("cek query $query ${article?.title}")
                if (article != null) {
                    if (article.title?.lowercase(Locale.ROOT)?.contains(lowerCaseQuery) == true) {
                        items.add(article)
                    }
                }
            }
        }

        notifyDataSetChanged()
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    init {
        ctx = context
    }

    internal inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var title: TextView
        var short_content: TextView
        var date: TextView
        var image: ImageView
        var lyt_parent: LinearLayout

        init {
            title = v.findViewById<View>(R.id.title) as TextView
            short_content = v.findViewById<View>(R.id.short_content) as TextView
            date = v.findViewById<View>(R.id.date) as TextView
            image = v.findViewById<View>(R.id.image) as ImageView
            lyt_parent = v.findViewById<View>(R.id.lyt_parent) as LinearLayout
        }
    }

    internal inner class ViewHolderHeader(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView
        var date: TextView
        var image: ImageView
        var lyt_parent: LinearLayout

        init {
            title = v.findViewById<View>(R.id.title) as TextView
            date = v.findViewById<View>(R.id.date) as TextView
            image = v.findViewById<View>(R.id.image) as ImageView
            lyt_parent = v.findViewById<View>(R.id.lyt_parent) as LinearLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View = when (viewType) {
            TYPE_HEADER -> {
                LayoutInflater.from(parent.context).inflate(R.layout.row_header, parent, false)
            }
            TYPE_ITEM -> {
                LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
            }
            TYPE_LOADING -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

        return when (viewType) {
            TYPE_HEADER -> ViewHolderHeader(v)
            TYPE_ITEM -> ViewHolder(v)
            TYPE_LOADING -> LoadingViewHolder(v)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    //    need to override this method
    override fun getItemViewType(position: Int): Int {
        return when {
            isPositionHeader(position) -> TYPE_HEADER
            items[position] == null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    fun getItem(position: Int): Article? {
        return items[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolderHeader -> {
                holder.title.text = header.title
                holder.date.text = header.publishedAt?.let { DateFormatLocale.covertToTimeDiff(it) }
                Picasso.with(ctx).load(header.urlToImage).into(holder.image)
                holder.lyt_parent.setOnClickListener { view ->
                    if ( mOnItemClickListener != null) {
                        mOnItemClickListener!!.onItemClick(view, header, position)
                    }
                }
            }
            is ViewHolder -> {
                val c: Article? = items[position]
                holder.title.text = c?.title
                holder.short_content.text = c?.content
                holder.date.text = c?.publishedAt?.let { DateFormatLocale.covertToTimeDiff(it) }
                Picasso.with(ctx).load(c?.urlToImage).into(holder.image)
                holder.lyt_parent.setOnClickListener { view ->
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener!!.onItemClick(view, c, position)
                    }
                }
            }
            is LoadingViewHolder -> {}
        }
    }
}