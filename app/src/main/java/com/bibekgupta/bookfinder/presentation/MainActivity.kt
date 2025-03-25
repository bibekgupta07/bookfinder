package com.bibekgupta.bookfinder.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bibekgupta.bookfinder.presentation.ui.theme.BookFinderTheme
import com.bibekgupta.bookfinder.utils.coodinator.MainApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookFinderTheme {
                MainApp()
            }
        }
    }
}


