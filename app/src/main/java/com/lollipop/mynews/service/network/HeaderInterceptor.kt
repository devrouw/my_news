package com.lollipop.mynews.service.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("X-Api-Key", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}