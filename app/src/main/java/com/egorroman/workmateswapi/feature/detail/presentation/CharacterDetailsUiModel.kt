package com.egorroman.workmateswapi.feature.detail.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Transgender
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import com.egorroman.workmateswapi.core.domain.model.Character

@Stable
internal data class CharacterDetailsUiModel(
    val name: String,
    val infoStats: List<DetailStatItem>,
    val appearanceStats: List<DetailStatItem>,
    val films: List<String>,
    val species: List<String>,
    val starships: List<String>,
    val vehicles: List<String>
)

internal data class DetailStatItem(
    val icon: ImageVector,
    val label: String,
    val value: String
)

internal fun Character.toDetailsUiModel() = CharacterDetailsUiModel(
    name = name,
    infoStats = listOf(
        DetailStatItem(Icons.Outlined.Cake, "Birth Year", birthYear),
        DetailStatItem(Icons.Outlined.Transgender, "Gender", gender)
    ),
    appearanceStats = listOf(
        DetailStatItem(Icons.Outlined.Height, "Height", "$height cm"),
        DetailStatItem(Icons.Outlined.MonitorWeight, "Mass", "$mass kg"),
        DetailStatItem(Icons.Outlined.Palette, "Skin", skinColor),
        DetailStatItem(Icons.Outlined.Face, "Hair", hairColor),
        DetailStatItem(Icons.Outlined.Visibility, "Eyes", eyeColor)
    ),
    films = films,
    species = species,
    starships = starships,
    vehicles = vehicles
)