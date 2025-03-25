package com.bibekgupta.bookfinder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.bibekgupta.bookfinder.data.local.entity.FavoriteBookEntity

@Dao
interface FavoriteBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(book: FavoriteBookEntity)

    @Query("DELETE FROM favorite_books WHERE id = :bookId")
    suspend fun removeFromFavorites(bookId: String)

    @Query("SELECT * FROM favorite_books ORDER BY addedAt DESC")
    fun getAllFavoriteBooks(): Flow<List<FavoriteBookEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE id = :bookId LIMIT 1)")
    fun isBookFavorite(bookId: String): Flow<Boolean>

}