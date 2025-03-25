package com.bibekgupta.bookfinder.data.repository

import com.bibekgupta.bookfinder.data.local.dao.FavoriteBookDao
import com.bibekgupta.bookfinder.data.local.entity.FavoriteBookEntity
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val favoriteBookDao: FavoriteBookDao,
    private val gson: Gson
) {

    suspend fun addToFavorites(book: Book) {
        val entity = FavoriteBookEntity(
            id = book.id,
            title = book.title,
            authors = gson.toJson(book.authors),
            publishedDate = book.publishedDate,
            description = book.description.toString(),
            coverImageUrl = book.coverImageUrl
        )
        favoriteBookDao.addToFavorites(entity)
    }

    suspend fun removeFromFavorites(bookId: String) {
        favoriteBookDao.removeFromFavorites(bookId)
    }

    fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao.getAllFavoriteBooks().map { entities ->
            entities.map { entity ->
                Book(
                    id = entity.id,
                    title = entity.title,
                    authors = gson.fromJson(entity.authors, Array<String>::class.java).toList(),
                    publishedDate = entity.publishedDate,
                    description = entity.description,
                    coverImageUrl = entity.coverImageUrl
                )
            }
        }
    }

    fun isBookFavorite(bookId: String): Flow<Boolean> {
        return favoriteBookDao.isBookFavorite(bookId)
    }



}