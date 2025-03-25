package com.bibekgupta.bookfinder.utils.coodinator

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bibekgupta.bookfinder.domain.remote.model.Book
import com.bibekgupta.bookfinder.presentation.details.BookDetailScreen
import com.bibekgupta.bookfinder.presentation.favorites.FavoritesScreen
import com.bibekgupta.bookfinder.presentation.homepage.BookScreen
import com.google.gson.Gson

interface AppCoordinator {
    fun navigateTo(route: String)
    fun navigateBack()
    fun popUpTo(route: String)
    fun navigateToBook()
    fun navigateToFavorites()
    fun navigateToBookDetails(book: Book)
}

class NavCoordinator(
    val navController: NavHostController
) : AppCoordinator {
    override fun navigateTo(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    override fun popUpTo(route: String) {
        // Implementation for popUpTo
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
        }
    }

    override fun navigateToBook() {
        navigateTo(NavRoutes.BOOK)
    }

    override fun navigateToFavorites() {
        navigateTo(NavRoutes.FAVORITES)
    }

    override fun navigateToBookDetails(book: Book) {
        // Convert book to JSON string
        val gson = Gson()
        val bookJson = gson.toJson(book)

        // Encode for URL safety
        val encodedBook = Uri.encode(bookJson)

        // Navigate with the encoded book
        navController.navigate("${NavRoutes.BOOK_DETAILS}/$encodedBook")
    }
}

@Composable
fun NavGraph(
    coordinator: NavCoordinator,
    padding: androidx.compose.foundation.layout.PaddingValues
) {
    NavHost(
        navController = coordinator.navController,
        startDestination = NavRoutes.BOOK
    ) {
        composable(NavRoutes.BOOK) {
            BookScreen(coordinator = coordinator, padding = padding)
        }

        composable(NavRoutes.FAVORITES) {
            FavoritesScreen(coordinator = coordinator, padding = padding)
        }

        composable(
            route = "${NavRoutes.BOOK_DETAILS}/{bookJson}",
            arguments = listOf(navArgument("bookJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookJson = backStackEntry.arguments?.getString("bookJson")
            // The DetailScreen will decode this
            BookDetailScreen(
                bookJson = bookJson.toString(),
                coordinator = coordinator
            )
        }
    }
}

object NavRoutes {
    const val BOOK = "book"
    const val BOOK_DETAILS = "book_details"
    const val FAVORITES = "favorites"

    val bottomNavItems = listOf(
        BottomNavItem(
            route = NavRoutes.BOOK,
            title = "Home",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = NavRoutes.FAVORITES,
            title = "Favorites",
            icon = Icons.Default.Favorite
        )
    )

}


data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    coordinator: NavCoordinator
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNav = currentRoute == NavRoutes.BOOK || currentRoute == NavRoutes.FAVORITES

    if (showBottomNav) {
        NavigationBar(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            NavRoutes.bottomNavItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(if (currentRoute == item.route) 1.2f else 1f),
                            tint = if (currentRoute == item.route)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            coordinator.navigateTo(item.route)
                        }
                    },
                    // Remove the default selection indicator
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val coordinator = NavCoordinator(navController)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, coordinator)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(coordinator, padding = innerPadding)
        }
    }
}