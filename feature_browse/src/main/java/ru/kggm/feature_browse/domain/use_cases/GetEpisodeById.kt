package ru.kggm.feature_browse.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.domain.repositories.EpisodeRepository
import javax.inject.Inject

class GetEpisodeById @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        episodeRepository.getById(id)
    }
}