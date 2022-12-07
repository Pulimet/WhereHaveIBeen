package net.alexandroid.where.model

data class Search(val items: List<SearchItem>)

data class SearchItem(val title: String)
