package com.egorroman.workmateswapi.core.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmDto(
    val title: String,
    val director: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("opening_crawl")
    val openingCrawl: String
)
