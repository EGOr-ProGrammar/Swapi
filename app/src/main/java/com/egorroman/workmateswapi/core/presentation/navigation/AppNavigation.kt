package com.egorroman.workmateswapi.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.egorroman.workmateswapi.feature.detail.presentation.charactersDetailEntry
import com.egorroman.workmateswapi.feature.list.presentation.charactersListEntry

@Composable
fun AppNavigation() {
    val navigationStack = rememberNavBackStack(Destination.CharacterList)

    NavDisplay(
        backStack = navigationStack,
        onBack = {
            if (navigationStack.size > 1) {
                navigationStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            charactersListEntry(
                onNavigate = { destination ->
                    navigationStack.add(destination)
                }
            )

            charactersDetailEntry(
                onBackClick = {
                    navigationStack.removeLastOrNull()
                }
            )
        }
    )
}