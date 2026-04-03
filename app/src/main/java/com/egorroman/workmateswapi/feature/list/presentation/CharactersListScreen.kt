package com.egorroman.workmateswapi.feature.list.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egorroman.workmateswapi.core.presentation.Dimensions // Наш новый импорт

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterListScreen(
    onCharacterClick: (String) -> Unit,
) {
    val viewModel: CharactersListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            viewModel.consumeError()
            snackbarHostState.showSnackbar(message = message, withDismissAction = true)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
        ) {
            CharactersHeader(
                searchQuery = state.searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) }
            )

            CharactersContent(
                state = state,
                onRefresh = { viewModel.refresh() },
                onCharacterClick = onCharacterClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersHeader(
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.PaddingMedium, vertical = Dimensions.PaddingSmall)
    ) {
        Text(
            text = "Star Wars",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = Dimensions.LetterSpacingTight
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Characters",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = Dimensions.PaddingSmall)
        )

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search by name...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange(""); focusManager.clearFocus() }) {
                                Icon(Icons.Default.Clear, null)
                            }
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier.fillMaxWidth()
        ) { }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersContent(
    state: CharactersListState,
    onRefresh: () -> Unit,
    onCharacterClick: (String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.characters.isEmpty() && !state.isLoading) {
            EmptyCharactersState(onRetry = onRefresh)
        } else {
            val listState = rememberLazyListState()
            val focusManager = LocalFocusManager.current

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingSmall),
                contentPadding = PaddingValues(Dimensions.PaddingMedium)
            ) {
                items(
                    items = state.characters,
                    key = { it.name }
                ) { character ->
                    CharacterCard(
                        character = character,
                        onClick = {
                            focusManager.clearFocus()
                            onCharacterClick(character.name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCharactersState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No characters found.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(Dimensions.PaddingMedium))
            Button(onClick = onRetry) {
                Text("Retry Load")
            }
        }
    }
}

@Composable
private fun CharacterCard(
    character: CharacterUiModel,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimensions.RadiusLarge),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.PaddingLarge)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = Dimensions.LetterSpacingMedium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Dimensions.PaddingMedium),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            val stats = listOf(
                Triple(Icons.Outlined.Height, "Height", "${character.height} cm"),
                Triple(Icons.Outlined.MonitorWeight, "Mass", "${character.mass} kg"),
                Triple(Icons.Outlined.Face, "Hair", character.hairColor),
                Triple(Icons.Outlined.Visibility, "Eyes", character.eyeColor)
            )

            Column(verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingMedium)) {
                stats.chunked(2).forEach { rowStats ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowStats.forEach { (icon, label, value) ->
                            StatBlock(
                                icon = icon,
                                label = label,
                                value = value,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBlock(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimensions.IconBoxSize)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(Dimensions.PaddingSmall)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(Dimensions.IconSizeSmall),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(Dimensions.PaddingMedium / 1.5f)) // Немного меньше стандартного

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontWeight = FontWeight.Normal
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}