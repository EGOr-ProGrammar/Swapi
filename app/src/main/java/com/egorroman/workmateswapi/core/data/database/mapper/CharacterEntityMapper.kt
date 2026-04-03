package com.egorroman.workmateswapi.core.data.database.mapper

import com.egorroman.workmateswapi.core.data.database.entity.CharacterEntity
import com.egorroman.workmateswapi.core.domain.model.Character

fun CharacterEntity.toDomainModel(): Character {
    return Character(
        birthYear = birthYear,
        created = created,
        edited = edited,
        eyeColor = eyeColor,
        films = films,
        gender = gender,
        hairColor = hairColor,
        height = height,
        homeworld = homeworld,
        mass = mass,
        name = name,
        skinColor = skinColor,
        species = species,
        starships = starships,
        vehicles = vehicles
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        name = name,
        birthYear = birthYear,
        created = created,
        edited = edited,
        eyeColor = eyeColor,
        films = films,
        gender = gender,
        hairColor = hairColor,
        height = height,
        homeworld = homeworld,
        mass = mass,
        skinColor = skinColor,
        species = species,
        starships = starships,
        vehicles = vehicles,
    )
}