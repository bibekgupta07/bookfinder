package com.bibekgupta.bookfinder.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.bibekgupta.bookfinder.utils.preference_manager.ThemeMode
import com.bibekgupta.bookfinder.utils.preference_manager.ThemePreferenceManager

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun BookFinderTheme(
    themePreferenceManager: ThemePreferenceManager? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Collect theme preference (moved outside remember)
    val themeMode by themePreferenceManager?.themeMode?.collectAsState(initial = ThemeMode.SYSTEM)
        ?: remember { mutableStateOf(ThemeMode.SYSTEM) }

    // Determine theme
    val useDarkTheme = remember(themeMode, darkTheme) {
        when (themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> darkTheme
            else -> darkTheme
        }
    }

    // Dynamic color
    val colorScheme = remember(useDarkTheme, dynamicColor, context) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                try {
                    if (useDarkTheme) dynamicDarkColorScheme(context)
                    else dynamicLightColorScheme(context)
                } catch (e: Exception) {
                    if (useDarkTheme) DarkColorScheme else LightColorScheme
                }
            }
            useDarkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}