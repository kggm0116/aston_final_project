package ru.kggm.feature_browse.presentation.ui.locations.list

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
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters
import ru.kggm.feature_browse.domain.repositories.LocationPagingSource
import ru.kggm.feature_browse.domain.use_cases.GetLocationPagingSource
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.ListNetworkState
import javax.inject.Inject

class LocationListViewModel @Inject constructor(
    private val getLocationPagingSource: GetLocationPagingSource
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    private val filterParametersFlow = MutableStateFlow(LocationPagingFilters.Default)
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
            locationPagingSource?.invalidate()
        }
    }

    fun setNameFilter(text: String) {
        filterParametersFlow.value.run {
            copy(nameQuery = text.ifEmpty { null }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun setDimensionFilter(text: String) {
        filterParametersFlow.value.run {
            copy(dimension = text.ifEmpty { null }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun setTypeFilter(text: String) {
        filterParametersFlow.value.run {
            copy(type = text.ifEmpty { null }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun applyFilters() {
        safeLaunch {
            locationPagingSource?.apply {
                invalidate()
            }
        }
    }

    private var locationPagingSource: LocationPagingSource? = null
    private fun createPagingSource(): LocationPagingSource {
        return getLocationPagingSource(filterParameters.value).also {
            locationPagingSource = it
        }
    }

    fun refreshPagingData() {
        safeLaunch {
            locationPagingSource?.apply {
                if (networkState.value != ListNetworkState.Lost)
                    clearCache()
                invalidate()
            }
            if (networkStateFlow.value == ListNetworkState.Restored)
                networkStateFlow.tryEmit(ListNetworkState.Normal)
        }
    }

    val locationPagingData = Pager(
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