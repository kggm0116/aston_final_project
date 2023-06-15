package ru.kggm.feature_browse.domain.repositories

import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilterCollection
import ru.kggm.feature_browse.domain.entities.CharacterPagingSource

interface CharacterRepository {
    fun characterPagingSource(
        searchString: String,
        filters: CharacterFilterCollection
    ): CharacterPagingSource

    suspend fun getById(id: Long): CharacterEntity
}