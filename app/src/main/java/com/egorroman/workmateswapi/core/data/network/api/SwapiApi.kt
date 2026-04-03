package com.egorroman.workmateswapi.core.data.network.api

import com.egorroman.workmateswapi.core.data.network.dto.FilmDto
import com.egorroman.workmateswapi.core.data.network.dto.PeopleDto
import com.egorroman.workmateswapi.core.data.network.dto.SpeciesDto
import com.egorroman.workmateswapi.core.data.network.dto.StarshipDto
import com.egorroman.workmateswapi.core.data.network.dto.VehicleDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SwapiApi {
    @GET("people/")
    suspend fun getAllCharacters(): Response<PeopleDto>

    @GET
    suspend fun getFilm(@Url url: String): Response<FilmDto>

    @GET
    suspend fun getSpecies(@Url url: String): Response<SpeciesDto>

    @GET
    suspend fun getStarship(@Url url: String): Response<StarshipDto>

    @GET
    suspend fun getVehicle(@Url url: String): Response<VehicleDto>

}