package com.dealhunt.app.data

import com.dealhunt.app.model.DealDetail
import com.dealhunt.app.model.GameInfo
import com.dealhunt.app.model.GameSearchResult
import com.dealhunt.app.model.PlatformPrice
import com.dealhunt.app.model.Store
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val BASE_URL = "https://www.cheapshark.com/api/1.0/"
    private const val CDN_BASE = "https://www.cheapshark.com"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: CheapSharkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CheapSharkApi::class.java)

    fun storeLogoUrl(logoPath: String): String = "$CDN_BASE$logoPath"
    fun dealUrl(dealId: String): String = "https://www.cheapshark.com/redirect?dealID=$dealId"
    fun steamStoreUrl(steamAppId: String): String = "https://store.steampowered.com/app/$steamAppId"
}

class GameRepository {

    private val api = NetworkClient.api
    private var storeCache: List<Store> = emptyList()

    suspend fun getStores(): List<Store> {
        if (storeCache.isEmpty()) {
            storeCache = api.getStores()
        }
        return storeCache
    }

    suspend fun searchGames(query: String): List<GameSearchResult> {
        return api.searchGames(title = query, limit = 60)
    }

    suspend fun getGameDetails(gameId: String): GameInfo {
        return api.getGameInfo(gameId = gameId)
    }

    suspend fun getGamePrices(gameId: String): List<PlatformPrice> {
        val stores = getStores()
        val storeMap = stores.associateBy { it.storeId }

        val gameInfo = api.getGameInfo(gameId = gameId)
        val deals = gameInfo.deals

        if (deals.isEmpty()) return emptyList()

        val minPrice = deals.minOfOrNull { it.price.toDoubleOrNull() ?: Double.MAX_VALUE } ?: 0.0

        return deals.map { deal ->
            val store = storeMap[deal.storeId]
            val price = deal.price.toDoubleOrNull() ?: 0.0
            val retail = deal.retailPrice.toDoubleOrNull() ?: price
            val savings = deal.savings.toDoubleOrNull() ?: 0.0

            PlatformPrice(
                storeId = deal.storeId,
                storeName = store?.storeName ?: "Store ${deal.storeId}",
                logoUrl = store?.let { NetworkClient.storeLogoUrl(it.images.logo) } ?: "",
                currentPrice = price,
                originalPrice = retail,
                savingsPercent = savings,
                dealId = deal.dealId,
                isBestDeal = price == minPrice
            )
        }.sortedBy { it.currentPrice }
    }

    suspend fun getFeaturedDeals(): List<DealDetail> {
        return api.getDealsByStores(
            storeIds = "1,25,7,11",
            pageSize = 30,
            sortBy = "Savings"
        )
    }
}
