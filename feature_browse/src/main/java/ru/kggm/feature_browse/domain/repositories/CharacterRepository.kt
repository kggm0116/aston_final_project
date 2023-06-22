package ru.kggm.feature_browse.domain.repositories

import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters

typealias CharacterPagingSource = FilterPagingSource<CharacterEntity, CharacterPagingFilters>

interface CharacterRepository {
    fun pagingSource(filterParameters: CharacterPagingFilters): CharacterPagingSource

    suspend fun getById(id: Int): CharacterEntity?
}