package com.egorroman.workmateswapi.core.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    @SerialName("birth_year")
    val birthYear: String,
    @SerialName("created")
    val created: String,
    @SerialName("edited")
    val edited: String,
    @SerialName("eye_color")
    val eyeColor: String,
    @SerialName("films")
    val films: List<String>,
    @SerialName("gender")
    val gender: String,
    @SerialName("hair_color")
    val hairColor: String,
    @SerialName("height")
    val height: String,
    @SerialName("homeworld")
    val homeworld: String,
    @SerialName("mass")
    val mass: String,
    @SerialName("name")
    val name: String,
    @SerialName("skin_color")
    val skinColor: String,
    @SerialName("species")
    val species: List<String>,
    @SerialName("starships")
    val starships: List<String>,
    @SerialName("url")
    val url: String,
    @SerialName("vehicles")
    val vehicles: List<String>
)