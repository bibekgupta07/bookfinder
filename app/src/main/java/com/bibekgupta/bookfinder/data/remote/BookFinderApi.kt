package com.bibekgupta.bookfinder.data.remote

import com.bibekgupta.bookfinder.data.remote.model.BookResponse
import com.bibekgupta.bookfinder.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface BookFinderApi {

    @GET("books/v1/volumes")
    suspend fun getBooksByCategory(
        @Query("q") category: String,
        @Query("key") apiKey: String = API_KEY
    ): BookResponse

}