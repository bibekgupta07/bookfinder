package com.bibekgupta.bookfinder.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibekgupta.bookfinder.data.repository.FavoritesRepository
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val gson: Gson
) : ViewModel() {

    private val _bookDetails = MutableStateFlow<Resource<Book>>(Resource.Loading())
    val bookDetails: StateFlow<Resource<Book>> get() = _bookDetails

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    fun setBookDetails(book: Book) {
        _bookDetails.value = Resource.Success(book)

        //check if book is favorite
        viewModelScope.launch {
            favoritesRepository.isBookFavorite(book.id).collectLatest { isFav ->
                _isFavorite.value = isFav
            }
        }
    }

    fun toggleFavorite() {
        val book = (_bookDetails.value as Resource.Success).data ?: return

        viewModelScope.launch {
            if (_isFavorite.value) {
                favoritesRepository.removeFromFavorites(book.id)
            } else {
                favoritesRepository.addToFavorites(book)
            }
        }
    }

    fun parseAndSetBookDetail(bookJson: String?) {
        bookJson?.let {
            try {
                val book = gson.fromJson(bookJson, Book::class.java)
                setBookDetails(book)
            } catch (e: Exception) {
                _bookDetails.value = Resource.Error("Error parsing book details")
            }
        } ?: run {
            _bookDetails.value = Resource.Error("Book details not found")

        }
    }


}