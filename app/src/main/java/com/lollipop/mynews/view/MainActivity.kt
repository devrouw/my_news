package com.lollipop.mynews.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lollipop.mynews.databinding.ActivityMainBinding
import com.lollipop.mynews.helper.Constant
import com.lollipop.mynews.service.model.Category
import com.lollipop.mynews.view.adapter.AdapterCategoryList
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: AdapterCategoryList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = AdapterCategoryList(this,Constant.Category.getData(this))
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : AdapterCategoryList.OnItemClickListener {
            override fun onItemClick(view: View?, obj: Category?, position: Int) {
                startActivity(Intent(this@MainActivity,SourceActivity::class.java)
                    .putExtra(Constant.IntentParam.CATEGORY,obj?.name))
            }
        })
    }
}