package com.lollipop.mynews.view

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.mynews.R
import com.lollipop.mynews.databinding.ActivityDetailBinding
import com.lollipop.mynews.databinding.ActivityMainBinding
import com.lollipop.mynews.helper.Constant

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val url by lazy { intent.getStringExtra(Constant.IntentParam.URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (url != null) {
            val webView: WebView = findViewById(R.id.webView)
            webView.loadUrl(url!!)
        } else {
            finish()
        }
    }

}