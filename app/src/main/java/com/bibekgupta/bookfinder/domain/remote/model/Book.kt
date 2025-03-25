package com.bibekgupta.bookfinder.domain.remote.model

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val coverImageUrl: String,
    val description: String?
)