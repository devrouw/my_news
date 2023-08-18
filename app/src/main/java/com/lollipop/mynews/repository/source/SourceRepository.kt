package com.lollipop.mynews.repository.source

import com.lollipop.mynews.service.network.ApiNetwork
import com.lollipop.mynews.service.model.Sources
import com.lollipop.mynews.service.network.ResultOfNetwork
import com.lollipop.mynews.service.network.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceRepository @Inject constructor(
    private val service: ApiNetwork
) : ISourceRepository {

    override suspend fun get(category: String): ResultOfNetwork<Sources> =
        SafeApiCall { service.category(category) }

}