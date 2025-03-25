package com.bibekgupta.bookfinder.presentation.details

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.presentation.ui.theme.BookFinderTheme
import com.bibekgupta.bookfinder.utils.Resource
import com.bibekgupta.bookfinder.utils.coodinator.NavCoordinator
import com.google.gson.Gson

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookJson: String,
    coordinator: NavCoordinator,
    viewModel: BookDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(bookJson) {
        bookJson?.let {
            val book = Gson().fromJson(bookJson, Book::class.java)
            viewModel.setBookDetails(book)
        }
    }

    // Observe the state from the ViewModel
    val bookDetails by viewModel.bookDetails.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    // Fetch book details when the screen is launched

    BookFinderTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Book Details") },
                    navigationIcon = {
                        IconButton(onClick = {
                            coordinator.navigateBack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {viewModel.toggleFavorite()}) {
                            Icon(
                                imageVector = if(isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                            )

                        }
                    }
                )
            }
        ) { paddingValues ->
            when (bookDetails) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    val book = (bookDetails as Resource.Success).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Book Cover
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = book?.coverImageUrl,
                                error = rememberAsyncImagePainter("https://via.placeholder.com/150")
                            ),
                            contentDescription = book?.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Book Title
                        Text(
                            text = book?.title ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Author(s)
                        Text(
                            text = "By ${book?.authors?.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Published Date
                        Text(
                            text = "Published on ${book?.publishedDate}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description
                        Text(
                            text = book?.description ?: "No description available.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                is Resource.Error -> {
                    // Show error message
                    val errorMessage = (bookDetails as Resource.Error).message
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage ?: "An error occurred",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//@Preview(showBackground = true)
//@Composable
//fun BookDetailScreenPreview() {
//    BookFinderTheme {
//        BookDetailScreen(
//            book = Book,
//            coordinator = NavCoordinator(rememberNavController()),
//            viewModel = hiltViewModel()
//        )
//    }
//}