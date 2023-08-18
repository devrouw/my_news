package com.lollipop.mynews.service.network

import com.lollipop.mynews.service.model.Articles
import com.lollipop.mynews.service.model.Sources
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNetwork {
    @GET("/v2/top-headlines/sources")
    suspend fun category(
        @Query("category") category : String,
    ) : Response<Sources>

    @GET("/v2/everything")
    suspend fun article(
        @Query("sources") sources : String,
        @Query("page") page : String,
    ) : Response<Articles>
}