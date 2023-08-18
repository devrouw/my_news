package com.lollipop.mynews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lollipop.mynews.di.abstraction.IoDispatcher
import com.lollipop.mynews.repository.article.ArticleRepository
import com.lollipop.mynews.repository.source.SourceRepository
import com.lollipop.mynews.service.model.Articles
import com.lollipop.mynews.service.model.Sources
import com.lollipop.mynews.service.network.ResultOfNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val articleResponse: LiveData<ResultOfNetwork<Articles>> get() = _articleResponse
    private val _articleResponse = MutableLiveData<ResultOfNetwork<Articles>>()

    fun get(source: String, page: Int){
        viewModelScope.launch(dispatcher) {
            try {
                _articleResponse.postValue(ResultOfNetwork.Loading(true))
                _articleResponse.postValue(repository.get(source, page.toString()))
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        _articleResponse.postValue(
                            ResultOfNetwork.ApiFailed(
                                throwable.code(),
                                "[HTTP] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                        throw CancellationException()
                    }
                    else -> {
                        Timber.d("cek throwable $throwable")
                        _articleResponse.postValue(
                            ResultOfNetwork.UnknownError(
                                throwable
                            )
                        )
                    }
                }
            }
        }
    }
}