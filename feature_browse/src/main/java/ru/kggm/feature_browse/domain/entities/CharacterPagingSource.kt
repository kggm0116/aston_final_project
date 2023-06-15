package ru.kggm.feature_browse.domain.entities

import androidx.paging.PagingSource

abstract class CharacterPagingSource: PagingSource<Int, CharacterEntity>() {

    abstract suspend fun invalidateCache()

    abstract suspend fun setFilters(search: String, filters: List<CharacterFilter>)
}