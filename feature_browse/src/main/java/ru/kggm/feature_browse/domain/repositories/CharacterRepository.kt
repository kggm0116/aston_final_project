package ru.kggm.feature_browse.domain.repositories

import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import ru.kggm.feature_browse.domain.paging.CharacterPagingFilters

typealias CharacterPagingSource = FilterPagingSource<CharacterEntity, CharacterPagingFilters>

interface CharacterRepository {
    fun characterPagingSource(filterParameters: CharacterPagingFilters): CharacterPagingSource

    suspend fun getById(id: Int): CharacterEntity
}