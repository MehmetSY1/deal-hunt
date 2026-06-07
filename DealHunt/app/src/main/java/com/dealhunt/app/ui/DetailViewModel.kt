package com.dealhunt.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dealhunt.app.data.GameRepository
import com.dealhunt.app.model.GameDetailUiState
import com.dealhunt.app.model.PlatformPrice
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel : ViewModel() {

    private val repository = GameRepository()

    private val _gameDetail = MutableLiveData<UiState<GameDetailUiState>>(UiState.Loading)
    val gameDetail: LiveData<UiState<GameDetailUiState>> = _gameDetail

    fun loadGameDetail(gameId: String) {
        viewModelScope.launch {
            _gameDetail.value = UiState.Loading
            try {
                repository.getStores()
                val info = repository.getGameDetails(gameId)
                val prices = repository.getGamePrices(gameId)

                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("tr"))
                val cheapestDate = if (info.cheapestPriceEver.date > 0) {
                    dateFormat.format(Date(info.cheapestPriceEver.date * 1000))
                } else "Bilinmiyor"

                // Get first deal for metacritic / steam rating
                val firstDeal = info.deals.firstOrNull()

                _gameDetail.value = UiState.Success(
                    GameDetailUiState(
                        title = info.info.title,
                        thumbnail = info.info.thumbnail,
                        steamAppId = info.info.steamAppId,
                        metacriticScore = "N/A",
                        steamRating = "N/A",
                        platformPrices = prices,
                        cheapestEver = "₺${String.format("%.2f", info.cheapestPriceEver.price.toDoubleOrNull() ?: 0.0)}",
                        cheapestEverDate = cheapestDate
                    )
                )
            } catch (e: Exception) {
                _gameDetail.value = UiState.Error("Oyun detayları yüklenemedi: ${e.message}")
            }
        }
    }

    fun refreshPrices(gameId: String) {
        loadGameDetail(gameId)
    }
}
