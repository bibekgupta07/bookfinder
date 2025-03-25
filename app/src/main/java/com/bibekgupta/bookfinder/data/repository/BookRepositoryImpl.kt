package com.bibekgupta.bookfinder.data.repository

import com.bibekgupta.bookfinder.data.remote.BookFinderApi
import com.bibekgupta.bookfinder.data.remote.model.toDomain
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: BookFinderApi
) : BookRepository {

    private var cachedBooks: List<Book> = emptyList()

    override suspend fun getBooksByCategory(category: String): List<Book> {
        val response = api.getBooksByCategory(category)
        return response.items.map { it.toDomain() }.also {
            cachedBooks = it // Cache the fetched books
        }
    }

    override suspend fun getBookDetails(bookId: String): Book {
        return cachedBooks.find { it.id == bookId }
            ?: throw NoSuchElementException("Book not found")
    }
}