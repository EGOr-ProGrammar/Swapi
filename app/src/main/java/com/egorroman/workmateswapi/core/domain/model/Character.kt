package com.egorroman.workmateswapi.core.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
data class Character(
    val birthYear: String,
    val created: String,
    val edited: String,
    val eyeColor: String,
    val films: List<String>,
    val gender: String,
    val hairColor: String,
    val height: String,
    val homeworld: String,
    val mass: String,
    val name: String,
    val skinColor: String,
    val species: List<String>,
    val starships: List<String>,
    val vehicles: List<String>
)