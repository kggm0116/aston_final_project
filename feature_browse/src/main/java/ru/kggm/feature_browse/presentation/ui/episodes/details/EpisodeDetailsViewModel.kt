package ru.kggm.feature_browse.presentation.ui.episodes.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.feature_browse.domain.use_cases.GetEpisodeById
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val getEpisodeById: GetEpisodeById
): ViewModel() {

    fun loadEpisode(id: Long) = safeLaunch {
        episodeFlow.tryEmit(LoadResult(null, LoadingState.Loading))
        when (val result = getEpisodeById(id.toInt())) {
            null -> episodeFlow.tryEmit(LoadResult(null, LoadingState.Error))
            else -> {
                val episode = result.toPresentationEntity()
                episodeFlow.tryEmit(
                    LoadResult(episode, LoadingState.Loaded)
                )
            }
        }
    }

    private val episodeFlow = MutableStateFlow(
        LoadResult<EpisodePresentationEntity?>(null, LoadingState.Loading)
    )
    val episode = episodeFlow.asStateFlow()
}