package com.egorroman.workmateswapi.feature.detail.domain

import com.egorroman.workmateswapi.core.domain.repository.CharactersRepository
import com.egorroman.workmateswapi.core.domain.model.Character
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetCharacterDetailsInteractor @Inject constructor(
    private val repository: CharactersRepository,
) {
    operator fun invoke(characterName: String): Flow<Character?> {
        return repository.getCharacterByName(characterName)
    }

}