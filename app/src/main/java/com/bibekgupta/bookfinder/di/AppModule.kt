package com.bibekgupta.bookfinder.di

import com.bibekgupta.bookfinder.data.remote.BookFinderApi
import com.bibekgupta.bookfinder.data.repository.BookRepositoryImpl
import com.bibekgupta.bookfinder.domain.repository.BookRepository
import com.bibekgupta.bookfinder.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideBookFinderApi(okHttpClient: OkHttpClient): BookFinderApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookFinderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(api: BookFinderApi): BookRepository {
        return BookRepositoryImpl(api)
    }




}