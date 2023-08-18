package com.lollipop.mynews.service.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Category(
    val name: String,
    val color: String,
    val icon: Int
)