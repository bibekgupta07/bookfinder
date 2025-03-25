package com.bibekgupta.bookfinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bibekgupta.bookfinder.data.local.dao.FavoriteBookDao
import com.bibekgupta.bookfinder.data.local.entity.FavoriteBookEntity

@Database(entities = [FavoriteBookEntity::class], version = 1, exportSchema = false)
abstract class BookFinderDatabase: RoomDatabase() {
    abstract fun favoriteBookDao(): FavoriteBookDao
}