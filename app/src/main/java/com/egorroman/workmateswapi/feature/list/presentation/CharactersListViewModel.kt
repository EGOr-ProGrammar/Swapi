package com.egorroman.workmateswapi.feature.list.presentation

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egorroman.workmateswapi.core.domain.model.Character
import com.egorroman.workmateswapi.feature.list.domain.LoadCharactersInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "CharactersListVM"

@Stable
internal data class CharactersListState(
    val searchQuery: String = "",
    val characters: List<CharacterUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
internal class CharactersListViewModel @Inject constructor(
    private val loadCharactersInteractor: LoadCharactersInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersListState())
    val state: StateFlow<CharactersListState> = combine(
        loadCharactersInteractor.getCharactersFlow(),
        _state
    ) { characters, currentUiState ->
        Log.d(
            TAG,
            "Filtering characters. Total in DB: ${characters.size}, Query: '${currentUiState.searchQuery}'"
        )
        val filtered = filterCharacters(currentUiState.searchQuery, characters)
        currentUiState.copy(
            characters = filtered.map { it.toUiModel() }
        )
    }
        .onStart {
            Log.i(TAG, "Flow started: triggering initial network sync")
            syncNetwork()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CharactersListState()
        )

    fun refresh() {
        Log.i(TAG, "Manual refresh requested by user")
        syncNetwork()
    }

    fun consumeError() {
        Log.v(TAG, "Error message consumed/cleared")
        _state.update { it.copy(errorMessage = null) }
    }

    private fun syncNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.isLoading) {
                Log.w(TAG, "Sync already in progress. Skipping redundant call.")
                return@launch
            }

            Log.d(TAG, "Starting network sync process...")
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = loadCharactersInteractor.syncCharacters()

            result.onFailure { error ->
                Log.e(TAG, "Sync failed with error", error)
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = mapErrorToUserMessage(error)
                    )
                }
            }
            result.onSuccess {
                Log.i(TAG, "Sync completed successfully")
                _state.update { it.copy(isLoading = false, errorMessage = null) }
            }
        }
    }

    private fun mapErrorToUserMessage(error: Throwable): String {
        val actualError = error.cause ?: error
        return when {
            actualError is IOException ->
                "Отсутствует подключение к интернету. Проверьте сеть."

            error.message?.contains("HTTP Error") == true ->
                "Ошибка на стороне сервера. Попробуйте обновить позже."

            error.message?.contains("Response body is null") == true ->
                "Сервер вернул пустой ответ."

            else ->
                "Произошла неизвестная ошибка: ${error.localizedMessage ?: "Нет данных"}"
        }
    }

    private fun filterCharacters(
        searchQuery: String,
        characters: List<Character>
    ): List<Character> {
        return if (searchQuery.isBlank()) {
            characters
        } else {
            characters.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    fun setSearchQuery(newQuery: String) {
        Log.v(TAG, "Updating search query to: '$newQuery'")
        _state.update { it.copy(searchQuery = newQuery) }
    }
}

private fun Character.toUiModel() = CharacterUiModel(
    birthYear = birthYear,
    eyeColor = eyeColor,
    gender = gender,
    hairColor = hairColor,
    height = height,
    mass = mass,
    name = name
)