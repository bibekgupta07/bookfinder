package com.bibekgupta.bookfinder.presentation.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.presentation.ui.theme.BookFinderTheme
import com.bibekgupta.bookfinder.utils.Resource
import com.bibekgupta.bookfinder.utils.coodinator.NavCoordinator
import com.bibekgupta.bookfinder.utils.preference_manager.ThemeMode
import com.bibekgupta.bookfinder.utils.preference_manager.ThemePreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    padding: PaddingValues,
    coordinator: NavCoordinator,
    viewModel: BookViewModel = hiltViewModel(),
    themePreferenceManager: ThemePreferenceManager? = null
) {
    // Observe the state from the ViewModel
    val books by viewModel.books.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val popularTechBooks by viewModel.popularTechBooks.collectAsState()

    // Collect theme mode as a State
    val themeMode by themePreferenceManager?.themeMode
        ?.collectAsState(initial = ThemeMode.SYSTEM)
        ?: remember { mutableStateOf(ThemeMode.SYSTEM) }

    // Determine dark mode state
    var isDarkMode by remember(themeMode) {
        mutableStateOf(
            when (themeMode) {
                ThemeMode.DARK -> false
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> false
            }
        )
    }

    // Categories
    val categories = remember {
        listOf(
            Category("tech", "Technology"),
            Category("scifi", "Sci-Fi"),
            Category("romance", "Romance"),
            Category("mystery", "Mystery"),
            Category("biography", "Biography")
        )
    }

    // Fetch books when the selected category changes
    LaunchedEffect(Unit) {
        viewModel.fetchPopularTechBooks()
    }

    // Fetch books when the selected category changes
    LaunchedEffect(selectedCategory) {
        viewModel.fetchBooksByCategory(selectedCategory)
    }

    BookFinderTheme(
        darkTheme = isDarkMode
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Book Explorer") },
                    actions = {
                        // Theme toggle button
                        IconButton(
                            onClick = {
                                isDarkMode = !isDarkMode
                                themePreferenceManager?.let { manager ->
                                    CoroutineScope(Dispatchers.IO).launch {
                                        manager.setThemeMode(
                                            if (isDarkMode) ThemeMode.DARK else ThemeMode.LIGHT
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = "Toggle theme"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Rest of your existing BookScreen implementation remains the same
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Popular Tech Books Section
                SectionTitle("Top Technology Books")

                when (popularTechBooks) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {
                        val books = (popularTechBooks as Resource.Success).data ?: emptyList()
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(books) { book ->
                                FeaturedBookCard(
                                    book,
                                    onClick = {
                                        coordinator.navigateToBookDetails(book = book)
                                    }

                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        val errorMessage = (popularTechBooks as Resource.Error).message
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage ?: "An error occurred",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                // Categories Section
                SectionTitle("Categories")

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category.id == selectedCategory,
                            onCategorySelected = {
                                viewModel.fetchBooksByCategory(category.name)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Books Grid for Selected Category
                when (books) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {
                        val bookList = (books as Resource.Success).data ?: emptyList()
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(bookList) { book ->
                                BookGridItem(
                                    book,
                                    onClick = {
                                        coordinator.navigateToBookDetails(book = book)
                                    }
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        val errorMessage = (books as Resource.Error).message
                        Box(
                            modifier = Modifier.fillMaxSize(),
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
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun FeaturedBookCard(
    book: Book,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp)
            .clickable {
                onClick()
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Book Cover
            Image(
                painter = rememberAsyncImagePainter(
                    model = book.coverImageUrl,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/150")
                ),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )

            // Book Info
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.authors.joinToString(", "),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onCategorySelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onCategorySelected() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
fun BookGridItem(
    book: Book,
    onClick: () -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable {
                onClick()
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Book Cover
            Image(
                painter = rememberAsyncImagePainter(
                    model = book.coverImageUrl,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/150")
                ),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )

            // Book Info
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = book.authors.joinToString(", "),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}


data class Category(
    val id: String,
    val name: String
)
