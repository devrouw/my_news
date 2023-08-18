package com.lollipop.mynews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lollipop.mynews.di.abstraction.IoDispatcher
import com.lollipop.mynews.repository.source.SourceRepository
import com.lollipop.mynews.service.model.Source
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
class SourceViewModel @Inject constructor(
    private val repository: SourceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val sourceResponse: LiveData<ResultOfNetwork<Sources>> get() = _sourceResponse
    private val _sourceResponse = MutableLiveData<ResultOfNetwork<Sources>>()

    fun get(category: String){
        viewModelScope.launch(dispatcher) {
            try {
                _sourceResponse.postValue(ResultOfNetwork.Loading(true))
                _sourceResponse.postValue(repository.get(category))
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        Timber.d("cek throwable ${throwable.message()}")
                        _sourceResponse.postValue(
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
                        _sourceResponse.postValue(
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