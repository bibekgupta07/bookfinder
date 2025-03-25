package com.bibekgupta.bookfinder.domain.usecase

import retrofit2.HttpException
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.domain.repository.BookRepository
import com.bibekgupta.bookfinder.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

//class GetBooksUseCase @Inject constructor(
//    private val repository: BookRepository
//) {
//    operator fun invoke(
//        category: String = "Technology"
//    ): Flow<Resource<List<Book>>> = flow {
//        try {
//            emit(Resource.Loading())
//            val books = repository.getBooksByCategory(category)
//            emit(Resource.Success(books))
//        } catch(e: HttpException) {
//            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
//
//        } catch (e: IOException){
//            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
//        } catch (e: Exception){
//            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
//        }
//    }
//}


class GetBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(
        category: String = "Technology"
    ): Flow<Resource<List<Book>>> = flow {
        try {
            emit(Resource.Loading())
            val books = repository.getBooksByCategory(category)
            emit(Resource.Success(books))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}