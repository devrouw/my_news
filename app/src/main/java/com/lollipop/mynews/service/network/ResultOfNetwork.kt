package com.lollipop.mynews.service.network

sealed class ResultOfNetwork<out T> {
    data class Success<out R>(val value: R) : ResultOfNetwork<R>()
    data class Empty(val test: String) : ResultOfNetwork<Nothing>()
    data class Loading(val isLoading: Boolean) : ResultOfNetwork<Nothing>()

    data class ApiFailed(
        val code: Int,
        val message: String?,
        val throwable: Throwable?
    ) : ResultOfNetwork<Nothing>()

    data class UnknownError(val throwable: Throwable?) : ResultOfNetwork<Nothing>()
}