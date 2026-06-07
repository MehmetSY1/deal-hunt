package com.dealhunt.app.model

import com.google.gson.annotations.SerializedName

// CheapShark API - Game search result
data class GameSearchResult(
    @SerializedName("gameID") val gameId: String,
    @SerializedName("steamAppID") val steamAppId: String?,
    @SerializedName("cheapest") val cheapest: String,
    @SerializedName("cheapestDealID") val cheapestDealId: String,
    @SerializedName("external") val title: String,
    @SerializedName("thumb") val thumbnail: String,
    @SerializedName("internalName") val internalName: String
)

// CheapShark API - Deal detail
data class DealDetail(
    @SerializedName("dealID") val dealId: String,
    @SerializedName("storeID") val storeId: String,
    @SerializedName("gameID") val gameId: String,
    @SerializedName("salePrice") val salePrice: String,
    @SerializedName("normalPrice") val normalPrice: String,
    @SerializedName("isOnSale") val isOnSale: String,
    @SerializedName("savings") val savings: String,
    @SerializedName("metacriticScore") val metacriticScore: String,
    @SerializedName("steamRatingText") val steamRatingText: String?,
    @SerializedName("steamRatingPercent") val steamRatingPercent: String?,
    @SerializedName("steamRatingCount") val steamRatingCount: String?,
    @SerializedName("releaseDate") val releaseDate: Long,
    @SerializedName("title") val title: String,
    @SerializedName("thumb") val thumbnail: String
)

// CheapShark API - Store info
data class Store(
    @SerializedName("storeID") val storeId: String,
    @SerializedName("storeName") val storeName: String,
    @SerializedName("isActive") val isActive: Int,
    @SerializedName("images") val images: StoreImages
)

data class StoreImages(
    @SerializedName("banner") val banner: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("icon") val icon: String
)

// CheapShark API - Game info detail
data class GameInfo(
    @SerializedName("info") val info: GameInfoDetail,
    @SerializedName("cheapestPriceEver") val cheapestPriceEver: CheapestPriceEver,
    @SerializedName("deals") val deals: List<GameDealItem>
)

data class GameInfoDetail(
    @SerializedName("title") val title: String,
    @SerializedName("steamAppID") val steamAppId: String?,
    @SerializedName("thumb") val thumbnail: String
)

data class CheapestPriceEver(
    @SerializedName("price") val price: String,
    @SerializedName("date") val date: Long
)

data class GameDealItem(
    @SerializedName("storeID") val storeId: String,
    @SerializedName("dealID") val dealId: String,
    @SerializedName("price") val price: String,
    @SerializedName("retailPrice") val retailPrice: String,
    @SerializedName("savings") val savings: String
)

// UI model for displaying platform prices
data class PlatformPrice(
    val storeId: String,
    val storeName: String,
    val logoUrl: String,
    val currentPrice: Double,
    val originalPrice: Double,
    val savingsPercent: Double,
    val dealId: String,
    val isBestDeal: Boolean = false
)

data class GameDetailUiState(
    val title: String,
    val thumbnail: String,
    val steamAppId: String?,
    val metacriticScore: String,
    val steamRating: String,
    val platformPrices: List<PlatformPrice>,
    val cheapestEver: String,
    val cheapestEverDate: String
)
