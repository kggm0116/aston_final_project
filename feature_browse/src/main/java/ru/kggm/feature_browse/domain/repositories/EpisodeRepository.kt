package ru.kggm.feature_browse.domain.repositories

import ru.kggm.feature_browse.domain.entities.EpisodeEntity
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters

typealias EpisodePagingSource = FilterPagingSource<EpisodeEntity, EpisodePagingFilters>

interface EpisodeRepository {
    fun pagingSource(filterParameters: EpisodePagingFilters): EpisodePagingSource

    suspend fun getById(id: Int): EpisodeEntity?
}