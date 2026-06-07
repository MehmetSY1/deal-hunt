package com.dealhunt.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dealhunt.app.data.GameRepository
import com.dealhunt.app.model.DealDetail
import com.dealhunt.app.model.GameSearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class MainViewModel : ViewModel() {

    private val repository = GameRepository()

    private val _searchResults = MutableLiveData<UiState<List<GameSearchResult>>>(UiState.Idle)
    val searchResults: LiveData<UiState<List<GameSearchResult>>> = _searchResults

    private val _featuredDeals = MutableLiveData<UiState<List<DealDetail>>>(UiState.Loading)
    val featuredDeals: LiveData<UiState<List<DealDetail>>> = _featuredDeals

    private var searchJob: Job? = null

    init {
        loadFeaturedDeals()
    }

    fun searchGames(query: String) {
        if (query.isBlank()) {
            _searchResults.value = UiState.Idle
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400) // debounce
            _searchResults.value = UiState.Loading
            try {
                val results = repository.searchGames(query)
                _searchResults.value = if (results.isEmpty()) {
                    UiState.Error("'$query' için sonuç bulunamadı")
                } else {
                    UiState.Success(results)
                }
            } catch (e: Exception) {
                _searchResults.value = UiState.Error("Bağlantı hatası: ${e.message}")
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _searchResults.value = UiState.Idle
    }

    private fun loadFeaturedDeals() {
        viewModelScope.launch {
            try {
                repository.getStores() // pre-cache stores
                val deals = repository.getFeaturedDeals()
                _featuredDeals.value = UiState.Success(deals)
            } catch (e: Exception) {
                _featuredDeals.value = UiState.Error("Öne çıkan fırsatlar yüklenemedi")
            }
        }
    }

    fun refreshFeaturedDeals() {
        _featuredDeals.value = UiState.Loading
        loadFeaturedDeals()
    }
}
