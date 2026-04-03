package com.egorroman.workmateswapi.core.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PeopleDto(
    val results: List<CharacterDto>
)