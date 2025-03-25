package com.bibekgupta.bookfinder.domain.repository

import com.bibekgupta.bookfinder.domain.remote.model.Book

interface BookRepository {

    suspend fun getBooksByCategory(category: String= "Technology"): List<Book>

    suspend fun getBookDetails(bookId: String): Book

}