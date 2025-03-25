package com.bibekgupta.bookfinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_books")
data class FavoriteBookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: String,
    val publishedDate: String,
    val description: String,
    val coverImageUrl: String,
    val addedAt: Long = System.currentTimeMillis()
)