package com.app.movieflix.ui.movies

import androidx.lifecycle.*
import com.app.movieflix.data.source.IDataRepository
import com.app.movieflix.pojo.NetworkMode
import com.app.movieflix.pojo.Result
import com.app.movieflix.utilities.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MoviesViewModel"

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val dataRepository: IDataRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun getMovies(networkMode: NetworkMode) {
        _dataLoading.postValue(true)
        val queryParams = mutableMapOf<String, String>("api_key" to Config.API_KEY)
        viewModelScope.launch(dispatcher) {
            dataRepository.getMoviesList(queryParams, networkMode).collect { result ->
                delay(100).also { _result.postValue(result) }
            }
        }.invokeOnCompletion {
            _dataLoading.postValue(false)
        }
    }

    fun filterMovie(searchQuery: String) {
        _dataLoading.postValue(true)
        viewModelScope.launch(dispatcher) {
            dataRepository.getFilterMoviesList(searchQuery).collect {
                _result.postValue(it)
            }
        }.invokeOnCompletion {
            _dataLoading.postValue(false)
        }
    }

    fun deleteMovieById(movieId: Int) {
        viewModelScope.launch(dispatcher) {
            dataRepository.deleteMovieByMovieId(movieId)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory @Inject constructor(
    private val dataRepository: IDataRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MoviesViewModel(dataRepository, dispatcher) as T)
}