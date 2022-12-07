package net.alexandroid.where.network

import net.alexandroid.where.model.Search
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleSearchService {
    // https://www.googleapis.com/customsearch/v1?
    // key=AIzaSyB3gh3OFjXqO2jLUs9mt_sM_PEkjuOuyfI
    // &cx=017576662512468239146:omuauf_lfve
    // &q=lectures

    @GET("customsearch/v1")
    suspend fun getSearchResult(
        @Query("q") query: String,
        @Query("key") apiKey: String = "AIzaSyB3gh3OFjXqO2jLUs9mt_sM_PEkjuOuyfI",
        @Query("cx") cx: String = "017576662512468239146:omuauf_lfve"
    ): Search
}