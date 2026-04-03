package com.egorroman.workmateswapi.core.domain.repository

import com.egorroman.workmateswapi.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getAllCharacterFlow(searchQuery: String = ""): Flow<List<Character>>

    // Запуск сетевого запроса и сохранение в БД
    suspend fun syncCharacters(): Result<Unit>
    fun getCharacterByName(name: String): Flow<Character?>
}