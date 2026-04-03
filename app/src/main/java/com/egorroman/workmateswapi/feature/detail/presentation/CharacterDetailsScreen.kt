package com.egorroman.workmateswapi.feature.detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egorroman.workmateswapi.core.presentation.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailsScreen(
    characterName: String,
    onBackClick: () -> Unit,
) {
    val viewModel: CharacterDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(characterName) {
        viewModel.loadCharacter(characterName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        state?.let { character ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(Dimensions.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingLarge)
            ) {
                item {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = Dimensions.LetterSpacingTight
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    DetailSection(title = "General Info", items = character.infoStats)
                }

                item {
                    DetailSection(title = "Appearance", items = character.appearanceStats)
                }

                if (character.films.isNotEmpty()) {
                    item { ListSection(title = "Films", items = character.films) }
                }

                if (character.starships.isNotEmpty()) {
                    item { ListSection(title = "Starships", items = character.starships) }
                }

                if (character.vehicles.isNotEmpty()) {
                    item { ListSection(title = "Vehicles", items = character.vehicles) }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DetailSection(title: String, items: List<DetailStatItem>) {
    Column {
        SectionTitle(title)
        Surface(
            shape = RoundedCornerShape(Dimensions.RadiusLarge),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(Dimensions.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingMedium)
            ) {
                items.forEach { item ->
                    DetailRow(item.icon, item.label, item.value)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(Dimensions.DetailIconBoxSize)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(Dimensions.PaddingSmall)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(Dimensions.IconSizeMedium - 4.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(Dimensions.PaddingSmall + 4.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ListSection(title: String, items: List<String>) {
    Column {
        SectionTitle(title)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingSmall)
        ) {
            items.forEach { text ->
                SuggestionChip(
                    onClick = {},
                    label = { Text(text) },
                    shape = RoundedCornerShape(Dimensions.RadiusMedium)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = Dimensions.PaddingSmall + 4.dp, start = 4.dp)
    )
}