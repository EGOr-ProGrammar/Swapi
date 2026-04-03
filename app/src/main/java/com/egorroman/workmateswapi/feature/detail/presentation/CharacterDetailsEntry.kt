package com.egorroman.workmateswapi.feature.detail.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.egorroman.workmateswapi.core.presentation.navigation.Destination

fun EntryProviderScope<NavKey>.charactersDetailEntry(onBackClick: () -> Unit) {
    entry<Destination.CharacterDetails> { details ->
        CharacterDetailsScreen(
            characterName = details.characterName,
            onBackClick = onBackClick,
        )
    }
}