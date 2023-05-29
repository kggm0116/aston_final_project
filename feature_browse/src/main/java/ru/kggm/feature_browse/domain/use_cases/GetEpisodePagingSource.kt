package ru.kggm.feature_browse.domain.use_cases

import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.domain.repositories.EpisodePagingSource
import ru.kggm.feature_browse.domain.repositories.EpisodeRepository
import javax.inject.Inject

class GetEpisodePagingSource @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    operator fun invoke(filters: EpisodePagingFilters) : EpisodePagingSource =
        episodeRepository.pagingSource(filters)
}