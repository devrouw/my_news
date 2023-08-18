package com.lollipop.mynews.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lollipop.mynews.databinding.ActivityMainBinding
import com.lollipop.mynews.helper.Constant
import com.lollipop.mynews.service.model.Category
import com.lollipop.mynews.service.model.Source
import com.lollipop.mynews.service.network.ResultOfNetwork
import com.lollipop.mynews.view.adapter.AdapterSourceList
import com.lollipop.mynews.viewmodel.SourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SourceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: AdapterSourceList
    private lateinit var _viewModel: SourceViewModel

    private val category by lazy { intent.getStringExtra(Constant.IntentParam.CATEGORY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the title of the ActionBar (AppBar)
        supportActionBar?.title = category

        initializeAdapter()
        initializeViewModel()
        observableLiveData()
    }

    private fun initializeAdapter(){
        mAdapter = AdapterSourceList(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickCallback(object : AdapterSourceList.OnItemClickCallback {
            override fun onClick(source: Source) {
                startActivity(
                    Intent(this@SourceActivity,ArticleActivity::class.java)
                    .putExtra(Constant.IntentParam.SOURCE_ID,source.id)
                        .putExtra(Constant.IntentParam.SOURCE_NAME,source.name))
            }
        })
    }

    private fun initializeViewModel(){
        _viewModel = ViewModelProvider(this)[SourceViewModel::class.java]
        category?.let { _viewModel.get(it) }
    }

    private fun observableLiveData(){
        _viewModel.sourceResponse.observe(this) { result ->
            when (result) {
                is ResultOfNetwork.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ResultOfNetwork.Success -> {
                    binding.progressBar.visibility = View.GONE
                    result.value.sources?.let { mAdapter.setData(it) }
                }
                is ResultOfNetwork.ApiFailed -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this@SourceActivity,"Network API Failed",Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this@SourceActivity,"Unknown error",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}