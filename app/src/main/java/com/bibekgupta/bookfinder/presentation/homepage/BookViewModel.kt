package com.bibekgupta.bookfinder.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.domain.usecase.GetBooksUseCase
import com.bibekgupta.bookfinder.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel() {

    // State for the list of books
    private val _books = MutableStateFlow<Resource<List<Book>>>(Resource.Loading())
    val books: StateFlow<Resource<List<Book>>> get() = _books

    // State for the popular tech books (domain Book)
    private val _popularTechBooks = MutableStateFlow<Resource<List<Book>>>(Resource.Loading())
    val popularTechBooks: StateFlow<Resource<List<Book>>> get() = _popularTechBooks

    // State for the selected category
    private val _selectedCategory = MutableStateFlow("Technology")
    val selectedCategory: StateFlow<String> get() = _selectedCategory

    // Fetch books based on the selected category
    fun fetchBooksByCategory(
        category: String
    ) {
        _selectedCategory.value = category
        getBooksUseCase(category)
            .onEach { result ->
                _books.value = result
            }
            .launchIn(viewModelScope)
    }

    // Fetch popular tech books
    fun fetchPopularTechBooks() {
        getBooksUseCase("technology")
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        // Limit to 3 books
                        val limitedBooks = result.data?.take(3) ?: emptyList()
                        _popularTechBooks.value = Resource.Success(limitedBooks)
                    }
                    is Resource.Error -> {
                        _popularTechBooks.value = Resource.Error(result.message ?: "An error occurred")
                    }
                    is Resource.Loading -> {
                        _popularTechBooks.value = Resource.Loading()
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}