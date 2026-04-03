package com.egorroman.workmateswapi.core.data.repository

import android.util.Log
import com.egorroman.workmateswapi.core.domain.model.Character
import com.egorroman.workmateswapi.core.data.database.dao.CharacterDao
import com.egorroman.workmateswapi.core.data.database.mapper.toDomainModel
import com.egorroman.workmateswapi.core.data.network.api.SwapiApi
import com.egorroman.workmateswapi.core.data.network.dto.CharacterDto
import com.egorroman.workmateswapi.core.data.network.mapper.toEntity
import com.egorroman.workmateswapi.core.domain.repository.CharactersRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.collections.map

private const val TAG = "CharactersRepo"

class CharactersRepositoryImpl @Inject constructor(
    private val api: SwapiApi,
    private val dao: CharacterDao
) : CharactersRepository {
    // Чтобы не прерывать корутины
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // Хранить повторы, чтобы не кэшировать их вновь
    private val urlToNameCache = ConcurrentHashMap<String, String>()

    override fun getAllCharacterFlow(searchQuery: String): Flow<List<Character>> {
        Log.d(TAG, "Observing character flow. Query: '$searchQuery'")
        return dao.getCharacters(searchQuery).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun syncCharacters(): Result<Unit> {
        return try {
            Log.i(TAG, "syncCharacters: Fetching main list from API")
            val response = api.getAllCharacters()

            if (!response.isSuccessful) {
                Log.e(TAG, "syncCharacters: API error ${response.code()} - ${response.message()}")
                return Result.failure(Exception("HTTP Error: ${response.code()} ${response.message()}"))
            }

            val charactersDtoList = response.body()?.results ?: run {
                Log.e(TAG, "syncCharacters: Received null body from API")
                return Result.failure(Exception("Response body is null"))
            }

            Log.d(TAG, "syncCharacters: Saving ${charactersDtoList.size} basic entities to DB")
            val initialEntities = charactersDtoList.map { dto ->
                dto.toEntity(
                    mappedFilms = dto.films,
                    mappedSpecies = dto.species,
                    mappedStarships = dto.starships,
                    mappedVehicles = dto.vehicles
                )
            }
            dao.replaceAllCharacters(initialEntities)

            Log.d(TAG, "syncCharacters: Starting background enrichment for details")
            loadDetailsInBackground(charactersDtoList)

            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "syncCharacters: Unhandled exception during sync", e)
            Result.failure(e)
        }
    }

    override fun getCharacterByName(name: String): Flow<Character?> {
        Log.d(TAG, "Fetching character by name: $name")
        return dao.getCharacterByName(name).map { it?.toDomainModel() }
    }

    private fun loadDetailsInBackground(characters: List<CharacterDto>) {
        repositoryScope.launch {
            for (dto in characters) {
                try {
                    Log.v(TAG, "Detail Loading: Processing ${dto.name}")
                    supervisorScope {
                        val filmsDef = dto.films.map {
                            async {
                                fetchCachedOrNetwork(
                                    it,
                                    api::getFilm
                                ) { f -> f.title }
                            }
                        }
                        val speciesDef = dto.species.map {
                            async {
                                fetchCachedOrNetwork(
                                    it,
                                    api::getSpecies
                                ) { s -> s.name }
                            }
                        }
                        val starshipsDef = dto.starships.map {
                            async {
                                fetchCachedOrNetwork(
                                    it,
                                    api::getStarship
                                ) { s -> s.name }
                            }
                        }
                        val vehiclesDef = dto.vehicles.map {
                            async {
                                fetchCachedOrNetwork(
                                    it,
                                    api::getVehicle
                                ) { v -> v.name }
                            }
                        }

                        val updatedEntity = dto.toEntity(
                            mappedFilms = filmsDef.awaitAll(),
                            mappedSpecies = speciesDef.awaitAll(),
                            mappedStarships = starshipsDef.awaitAll(),
                            mappedVehicles = vehiclesDef.awaitAll()
                        )

                        dao.insertCharacters(listOf(updatedEntity))
                        Log.v(TAG, "Detail Loading: Successfully updated ${dto.name}")
                    }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    Log.w(
                        TAG,
                        "Detail Loading: Failed for ${dto.name}. Skipping. Reason: ${e.message}"
                    )
                }
            }
            Log.i(TAG, "Detail Loading: Background process finished for the entire list")
        }
    }

    private suspend inline fun <T> fetchCachedOrNetwork(
        url: String,
        crossinline apiCall: suspend (String) -> Response<T>,
        crossinline extractName: (T) -> String
    ): String {
        urlToNameCache[url]?.let { return it }

        return try {
            val response = apiCall(url)
            if (response.isSuccessful) {
                val name = response.body()?.let(extractName) ?: url
                urlToNameCache[url] = name
                name
            } else {
                Log.w(
                    TAG,
                    "fetchCachedOrNetwork: Failed for $url. Response code: ${response.code()}"
                )
                url
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "fetchCachedOrNetwork: Error fetching $url", e)
            url
        }
    }
}