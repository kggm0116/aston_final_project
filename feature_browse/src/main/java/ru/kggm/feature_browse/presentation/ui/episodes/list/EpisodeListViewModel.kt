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
import ru.kggm.feature_browse.presentation.ui.shared.ListNetworkState
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

    fun setIds(ids: List<Int>?) {
        if (ids == filterParametersFlow.value.ids)
            return

        safeLaunch {
            filterParametersFlow.value.run {
                copy(ids = ids).let { newParameters ->
                    filterParametersFlow.tryEmit(newParameters)
                }
            }
            episodePagingSource?.invalidate()
        }
    }

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
            episodePagingSource?.apply {
                invalidate()
            }
        }
    }

    private var episodePagingSource: EpisodePagingSource? = null
    private fun createPagingSource(): EpisodePagingSource {
        return getEpisodePagingSource(filterParameters.value).also {
            episodePagingSource = it
        }
    }

    fun refreshPagingData() {
        safeLaunch {
            episodePagingSource?.apply {
                if (networkState.value != ListNetworkState.Lost)
                    clearCache()
                invalidate()
            }
            if (networkStateFlow.value == ListNetworkState.Restored)
                networkStateFlow.tryEmit(ListNetworkState.Normal)
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

    private val networkStateFlow = MutableStateFlow(ListNetworkState.Normal)
    val networkState = networkStateFlow.asStateFlow()

    fun onNetworkLost() {
        if (networkStateFlow.value != ListNetworkState.Lost)
            networkStateFlow.tryEmit(ListNetworkState.Lost)
    }

    fun onNetworkAvailable() {
        if (networkStateFlow.value == ListNetworkState.Lost)
            networkStateFlow.tryEmit(ListNetworkState.Restored)
    }
}