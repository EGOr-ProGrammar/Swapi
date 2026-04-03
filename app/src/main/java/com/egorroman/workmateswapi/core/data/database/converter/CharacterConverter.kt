package com.egorroman.workmateswapi.core.data.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class CharacterConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return Json.encodeToString(list ?: emptyList())
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        return if (data.isNullOrEmpty()) emptyList() else Json.decodeFromString(data)
    }
}