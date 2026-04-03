package com.egorroman.workmateswapi.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.egorroman.workmateswapi.core.data.database.converter.CharacterConverter
import com.egorroman.workmateswapi.core.data.database.dao.CharacterDao
import com.egorroman.workmateswapi.core.data.database.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(CharacterConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
}