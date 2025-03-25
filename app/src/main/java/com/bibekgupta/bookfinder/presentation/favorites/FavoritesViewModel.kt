package com.bibekgupta.bookfinder.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibekgupta.bookfinder.data.repository.FavoritesRepository
import com.bibekgupta.bookfinder.domain.remote.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
): ViewModel() {
    private val _favorites = MutableStateFlow<List<Book>>(emptyList())
    val favorites: StateFlow<List<Book>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getFavoriteBooks().collectLatest { books ->
                _favorites.value = books
            }
        }
    }

    fun removeFromFavorites(bookId: String) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(bookId)
        }

    }

}