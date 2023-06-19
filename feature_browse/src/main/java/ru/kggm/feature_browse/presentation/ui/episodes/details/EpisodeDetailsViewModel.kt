package ru.kggm.feature_browse.presentation.ui.episodes.details

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.use_cases.GetEpisodeById
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val getEpisodeById: GetEpisodeById
): ViewModel() {

    init {
        Log.i(classTag(), "Initialized")
    }

    override fun onCleared() {
        Log.i(classTag(), "Cleared")
        super.onCleared()
    }

    fun loadEpisode(id: Long) = safeLaunch {
        getEpisodeById(id.toInt()).let {
            episodeFlow.tryEmit(it.toPresentationEntity())
        }
    }

    private val episodeFlow = MutableStateFlow<EpisodePresentationEntity?>(null)
    val episode = episodeFlow.asStateFlow()
}