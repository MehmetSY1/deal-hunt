package com.dealhunt.app.data

import com.dealhunt.app.model.DealDetail
import com.dealhunt.app.model.GameInfo
import com.dealhunt.app.model.GameSearchResult
import com.dealhunt.app.model.Store
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {

    @GET("games")
    suspend fun searchGames(
        @Query("title") title: String,
        @Query("limit") limit: Int = 60,
        @Query("exact") exact: Int = 0
    ): List<GameSearchResult>

    @GET("games")
    suspend fun getGameInfo(
        @Query("id") gameId: String
    ): GameInfo

    @GET("deals")
    suspend fun getDeals(
        @Query("title") title: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("sortBy") sortBy: String = "Price",
        @Query("storeID") storeId: String? = null
    ): List<DealDetail>

    @GET("stores")
    suspend fun getStores(): List<Store>

    @GET("deals")
    suspend fun getDealsByStores(
        @Query("storeID") storeIds: String = "1,25",  // 1=Steam, 25=Epic
        @Query("pageSize") pageSize: Int = 20,
        @Query("sortBy") sortBy: String = "Savings"
    ): List<DealDetail>
}
