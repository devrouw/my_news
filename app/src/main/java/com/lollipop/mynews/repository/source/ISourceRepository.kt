package com.lollipop.mynews.repository.source

import com.lollipop.mynews.service.model.Sources
import com.lollipop.mynews.service.network.ResultOfNetwork

interface ISourceRepository {
    suspend fun get(category: String) : ResultOfNetwork<Sources>
}