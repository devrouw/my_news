package com.lollipop.mynews.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.mynews.R
import com.lollipop.mynews.databinding.ActivityMainBinding
import com.lollipop.mynews.helper.Constant
import com.lollipop.mynews.service.model.Article
import com.lollipop.mynews.service.model.Articles
import com.lollipop.mynews.service.network.ResultOfNetwork
import com.lollipop.mynews.view.adapter.AdapterArticleList
import com.lollipop.mynews.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: AdapterArticleList
    private lateinit var _viewModel: ArticleViewModel

    private var results: MutableList<Article> = mutableListOf()

    private val sourceId by lazy { intent.getStringExtra(Constant.IntentParam.SOURCE_ID) }
    private val sourceName by lazy { intent.getStringExtra(Constant.IntentParam.SOURCE_NAME) }

    private var cursor = 1
    private var loadMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = sourceName

        initializeAdapter()
        initializeViewModel()
        observableLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        // Handle query submission
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = binding.recyclerView.adapter as? AdapterArticleList
                adapter?.filter(newText.orEmpty())
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Handle search icon press
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeAdapter(){
        mAdapter = AdapterArticleList(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : AdapterArticleList.OnItemClickListener {
            override fun onItemClick(view: View?, obj: Article?, position: Int) {
                startActivity(
                    Intent(this@ArticleActivity,DetailActivity::class.java)
                    .putExtra(Constant.IntentParam.URL,obj?.url))
            }
        })
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loadMore && cursor != 1) {
                        loadMore = true
                        mAdapter.showLoadingIndicator()
                        sourceId?.let {
                            _viewModel.get(
                                it,
                                cursor
                            )
                        }
                    }
                }
            }
        })

    }

    private fun initializeViewModel(){
        _viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        sourceId?.let { _viewModel.get(it,cursor) }
    }

    private fun observableLiveData(){
        _viewModel.articleResponse.observe(this) { result ->
            when (result) {
                is ResultOfNetwork.Loading -> {
                    if(!loadMore){
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                is ResultOfNetwork.Success -> {
                    mAdapter.hideLoadingIndicator()
                    binding.progressBar.visibility = View.GONE
                    if(!result.value.articles.isNullOrEmpty()){
                        results = (result.value.articles as MutableList<Article>?)!!

                        if (!loadMore) {
                            mAdapter.clearData()
                        }
                        results.let { mAdapter.setData(it) }
                        results.let { mAdapter.setHeader(it[result.value.articles.size - 1]) }

                        cursor += 1
                        loadMore = false
                    }
                }
                is ResultOfNetwork.ApiFailed -> {
                    mAdapter.hideLoadingIndicator()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ArticleActivity,"Network API Failed",Toast.LENGTH_LONG).show()
                }
                else -> {
                    mAdapter.hideLoadingIndicator()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ArticleActivity,"Unknown error",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}