package com.lollipop.mynews.repository.article

import com.lollipop.mynews.repository.source.ISourceRepository
import com.lollipop.mynews.service.model.Articles
import com.lollipop.mynews.service.model.Sources
import com.lollipop.mynews.service.network.ApiNetwork
import com.lollipop.mynews.service.network.ResultOfNetwork
import com.lollipop.mynews.service.network.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val service: ApiNetwork
) : IArticleRepository {

    override suspend fun get(source: String, page: String): ResultOfNetwork<Articles> =
        SafeApiCall { service.article(source, page) }
}