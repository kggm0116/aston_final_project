package ru.kggm.feature_browse.presentation.ui.characters.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.domain.repositories.EpisodePagingSource
import ru.kggm.feature_browse.domain.use_cases.GetCharacterById
import ru.kggm.feature_browse.domain.use_cases.GetEpisodeById
import ru.kggm.feature_browse.domain.use_cases.GetEpisodePagingSource
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.domain.use_cases.GetLocationPagingSource
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListViewModel
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterById: GetCharacterById,
//    private val getEpisodeById: GetEpisodeById,
//    private val getLocationById: GetLocationById,
//    private val getEpisodePagingSource: GetEpisodePagingSource
): ViewModel() {

    init {
        Log.i(classTag(), "Initialized")
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun onCleared() {
        Log.i(classTag(), "Cleared")
        super.onCleared()
    }

    fun loadCharacter(id: Long) = safeLaunch {
        getCharacterById(id.toInt()).let {
            characterFlow.tryEmit(it.toPresentationEntity())
        }
    }

    private val characterFlow = MutableStateFlow<CharacterPresentationEntity?>(null)
    val character = characterFlow.asStateFlow()
//        .onEach {
//        episodePagingSource?.invalidate()
//    }

//    private var episodePagingSource: EpisodePagingSource? = null
//    private fun createPagingSource(): EpisodePagingSource {
//        return getEpisodePagingSource(
//            EpisodePagingFilters(
//                ids = characterFlow.value?.episodeIds ?: emptyList()
//            )
//        ).also { episodePagingSource = it}
//    }
//
//    val episodePagingData = Pager(
//        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
//        pagingSourceFactory = { createPagingSource() },
//    )
//        .flow
//        .map { data ->
//            data.map { it.toPresentationEntity() }
//        }
//        .cachedIn(viewModelScope)
}