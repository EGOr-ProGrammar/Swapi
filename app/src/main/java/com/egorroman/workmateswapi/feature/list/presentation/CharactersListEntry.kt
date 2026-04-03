package com.egorroman.workmateswapi.feature.list.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.egorroman.workmateswapi.core.presentation.navigation.Destination

fun EntryProviderScope<NavKey>.charactersListEntry(onNavigate: (Destination) -> Unit) {
    entry<Destination.CharacterList> {
        CharacterListScreen(
            onCharacterClick = { characterName ->
                onNavigate(
                    Destination.CharacterDetails(
                        characterName = characterName
                    )
                )
            }
        )
    }
}