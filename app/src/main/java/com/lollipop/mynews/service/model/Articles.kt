package com.lollipop.mynews.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Articles (
    val status: String,
    val totalResults: Int,
    val articles: List<Article>? = null
)