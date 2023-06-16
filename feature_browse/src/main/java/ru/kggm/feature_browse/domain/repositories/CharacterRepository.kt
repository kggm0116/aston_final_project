package ru.kggm.feature_browse.domain.repositories

import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters
import ru.kggm.feature_browse.domain.entities.CharacterPagingSource

interface CharacterRepository {
    fun characterPagingSource(filterParameters: CharacterFilterParameters): CharacterPagingSource

    suspend fun getById(id: Long): CharacterEntity
}