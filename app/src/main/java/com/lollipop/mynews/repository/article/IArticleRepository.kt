package com.lollipop.mynews.repository.article

import com.lollipop.mynews.service.model.Articles
import com.lollipop.mynews.service.model.Sources
import com.lollipop.mynews.service.network.ResultOfNetwork

interface IArticleRepository {
    suspend fun get(source: String, page: String) : ResultOfNetwork<Articles>
}