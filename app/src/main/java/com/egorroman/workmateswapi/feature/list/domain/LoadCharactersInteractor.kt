package com.egorroman.workmateswapi.feature.list.domain

import com.egorroman.workmateswapi.core.domain.repository.CharactersRepository
import com.egorroman.workmateswapi.core.domain.model.Character
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class LoadCharactersInteractor @Inject constructor(
    private val repository: CharactersRepository
) {
    fun getCharactersFlow(query: String = ""): Flow<List<Character>> {
        return repository.getAllCharacterFlow(query)
    }

    suspend fun syncCharacters(): Result<Unit> {
        return repository.syncCharacters()
    }
}