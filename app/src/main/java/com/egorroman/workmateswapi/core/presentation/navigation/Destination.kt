package com.egorroman.workmateswapi.core.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination : NavKey {
    @Serializable
    data object CharacterList : Destination()

    @Serializable
    data class CharacterDetails(val characterName: String) : Destination()
}