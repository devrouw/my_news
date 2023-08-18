package com.lollipop.mynews.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sources(
    val status: String,
    val sources: List<Source>? = null
)
