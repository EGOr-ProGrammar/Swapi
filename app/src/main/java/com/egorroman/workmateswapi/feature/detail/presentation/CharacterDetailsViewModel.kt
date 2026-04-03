package com.egorroman.workmateswapi.feature.detail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egorroman.workmateswapi.feature.detail.domain.GetCharacterDetailsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "CharDetailsVM"

@HiltViewModel
internal class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterDetailsInteractor: GetCharacterDetailsInteractor
) : ViewModel() {

    private val _characterName = MutableStateFlow<String?>(null)

    val state: StateFlow<CharacterDetailsUiModel?> = _characterName
        .filterNotNull()
        .onEach { name -> Log.d(TAG, "New character requested: $name") }
        .flatMapLatest { name ->
            getCharacterDetailsInteractor(name)
        }
        .map { character ->
            if (character == null) {
                Log.w(TAG, "Character data is null for requested name")
            } else {
                Log.v(TAG, "Mapping character details UI for: ${character.name}")
            }
            character?.toDetailsUiModel()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun loadCharacter(name: String) {
        if (_characterName.value == name) {
            Log.v(TAG, "Character '$name' already active. Ignoring load call.")
            return
        }
        Log.i(TAG, "Setting active character name: $name")
        _characterName.value = name
    }
}