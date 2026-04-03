package com.egorroman.workmateswapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.egorroman.workmateswapi.core.presentation.navigation.AppNavigation
import com.egorroman.workmateswapi.core.presentation.theme.WorkmateSwapiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkmateSwapiTheme {
                AppNavigation()
            }
        }
    }
}