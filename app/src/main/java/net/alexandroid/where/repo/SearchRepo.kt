package net.alexandroid.where.repo

import net.alexandroid.where.network.GoogleSearchService

class SearchRepo(private val googleSearchService: GoogleSearchService) {
    suspend fun getSearchResults(query: String) = googleSearchService.getSearchResult(query)
}