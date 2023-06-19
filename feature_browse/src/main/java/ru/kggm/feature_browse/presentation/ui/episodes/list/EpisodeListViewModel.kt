package ru.kggm.feature_browse.presentation.ui.episodes.list

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
import kotlinx.coroutines.runBlocking
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.domain.repositories.EpisodePagingSource
import ru.kggm.feature_browse.domain.use_cases.GetEpisodePagingSource
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class EpisodeListViewModel @Inject constructor(
    private val getEpisodePagingSource: GetEpisodePagingSource
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    private val filterParametersFlow = MutableStateFlow(EpisodePagingFilters.Default)
    val filterParameters = filterParametersFlow.asStateFlow()

    fun setNameFilter(text: String) {
        filterParametersFlow.value.run {
            copy(nameQuery = text.ifEmpty { null }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun setCodeFilter(text: String) {
        filterParametersFlow.value.run {
            copy(code = text.ifEmpty { null }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun applyFilters() {
        safeLaunch {
            characterPagingSource?.apply {
                invalidate()
            }
        }
    }

    private var characterPagingSource: EpisodePagingSource? = null
    private fun createPagingSource(): EpisodePagingSource {
        return getEpisodePagingSource(filterParameters.value).also {
            characterPagingSource = it
        }
    }

    fun refreshPagingData() {
        safeLaunch {
            characterPagingSource?.apply {
                if (networkState.value != NetworkState.Lost)
                    clearCache()
                invalidate()
            }
        }
    }

    val episodePagingData = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { createPagingSource() },
    )
        .flow
        .map { data ->
            data.map { it.toPresentationEntity() }
        }
        .cachedIn(viewModelScope)

    enum class NetworkState { Normal, Lost, Restored }
    private val networkStateFlow = MutableStateFlow(NetworkState.Normal)
    val networkState = networkStateFlow.asStateFlow()

    fun onNetworkLost() {
        if (networkStateFlow.value != NetworkState.Lost)
            runBlocking { networkStateFlow.emit(NetworkState.Lost) }
    }

    fun onNetworkRestored() {
        if (networkStateFlow.value == NetworkState.Lost)
            runBlocking { networkStateFlow.emit(NetworkState.Restored) }
    }

    fun onDataRefreshed() {
        if (networkStateFlow.value == NetworkState.Restored)
            runBlocking { networkStateFlow.emit(NetworkState.Normal) }
    }
}