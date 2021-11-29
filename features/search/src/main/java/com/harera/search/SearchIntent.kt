package com.harera.search

sealed class SearchIntent() {
    data class Search(val data : String) : SearchIntent()
}
