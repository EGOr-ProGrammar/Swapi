package com.egorroman.workmateswapi.core.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class StarshipDto(
    val name: String,
    val model: String,
)
