package com.egorroman.workmateswapi.feature.list.presentation

import androidx.compose.runtime.Stable

@Stable
internal data class CharacterUiModel(
    val birthYear: String,
    val eyeColor: String,
    val gender: String,
    val hairColor: String,
    val height: String,
    val mass: String,
    val name: String,
)
