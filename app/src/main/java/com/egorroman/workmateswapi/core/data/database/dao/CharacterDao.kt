package com.egorroman.workmateswapi.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.egorroman.workmateswapi.core.data.database.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters WHERE name LIKE '%' || :searchQuery || '%'")
    fun getCharacters(searchQuery: String): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    suspend fun clearCharacters()

    @Transaction
    suspend fun replaceAllCharacters(characters: List<CharacterEntity>) {
        clearCharacters()
        insertCharacters(characters)
    }

    @Query("SELECT * FROM characters WHERE name = :name")
    fun getCharacterByName(name: String): Flow<CharacterEntity?>
}