package com.egorroman.workmateswapi.core.data.network.mapper

import com.egorroman.workmateswapi.core.data.database.entity.CharacterEntity
import com.egorroman.workmateswapi.core.data.network.dto.CharacterDto

fun CharacterDto.toEntity(
    mappedFilms: List<String>,
    mappedSpecies: List<String>,
    mappedStarships: List<String>,
    mappedVehicles: List<String>
): CharacterEntity {
    return CharacterEntity(
        name = name,
        birthYear = birthYear,
        created = created,
        edited = edited,
        eyeColor = eyeColor,
        films = mappedFilms,
        gender = gender,
        hairColor = hairColor,
        height = height,
        homeworld = homeworld,
        mass = mass,
        skinColor = skinColor,
        species = mappedSpecies,
        starships = mappedStarships,
        vehicles = mappedVehicles
    )
}