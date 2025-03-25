package com.bibekgupta.bookfinder.di

import android.content.Context
import androidx.room.Room
import com.bibekgupta.bookfinder.data.local.BookFinderDatabase
import com.bibekgupta.bookfinder.data.local.dao.FavoriteBookDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBookFinderDatabase(
        @ApplicationContext context: Context
    ): BookFinderDatabase {
        return Room.databaseBuilder(
            context,
            BookFinderDatabase::class.java,
            "bookfinder_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteBookDao(database: BookFinderDatabase) : FavoriteBookDao {
        return database.favoriteBookDao()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

}