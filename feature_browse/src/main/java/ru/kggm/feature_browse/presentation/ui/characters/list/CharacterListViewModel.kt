package ru.kggm.feature_browse.presentation.ui.characters.list

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
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters
import ru.kggm.feature_browse.domain.repositories.CharacterPagingSource
import ru.kggm.feature_browse.domain.use_cases.GetCharacterPagingSource
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.ListNetworkState
import javax.inject.Inject

class CharacterListViewModel @Inject constructor(
    private val getCharacterPagingSource: GetCharacterPagingSource
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    private val filterParametersFlow = MutableStateFlow(CharacterPagingFilters.Default)
    val filterParameters = filterParametersFlow.asStateFlow()

    fun setIds(ids: List<Int>?) {
        if (ids == filterParametersFlow.value.ids)
            return

        safeLaunch {
            filterParametersFlow.value.run {
                copy(ids = ids).let { newParameters ->
                    filterParametersFlow.emit(newParameters)
                }
            }
            characterPagingSource?.invalidate()
        }
    }

    fun cycleGender() {
        filterParametersFlow.value.run {
            copy(gender = when (gender) {
                CharacterEntity.Gender.Female -> CharacterEntity.Gender.Male
                CharacterEntity.Gender.Male -> CharacterEntity.Gender.Genderless
                CharacterEntity.Gender.Genderless -> CharacterEntity.Gender.Unknown
                CharacterEntity.Gender.Unknown -> null
                null -> CharacterEntity.Gender.Female
            }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun cycleStatus() {
        filterParametersFlow.value.run {
            copy(status = when (status) {
                CharacterEntity.Status.Alive -> CharacterEntity.Status.Dead
                CharacterEntity.Status.Dead -> CharacterEntity.Status.Unknown
                CharacterEntity.Status.Unknown -> null
                null -> CharacterEntity.Status.Alive
            }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun setNameFilter(text: String) {
        filterParametersFlow.value.run {
            copy(nameQuery = text.ifEmpty { null }).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
            }
        }
    }

    fun setSpeciesFilter(text: String) {
        filterParametersFlow.value.run {
            copy(species = text.ifEmpty { null }).let { newParameters ->
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
            characterPagingSource?.apply {
                invalidate()
            }
        }
    }

    private var characterPagingSource: CharacterPagingSource? = null
    private fun createPagingSource(): CharacterPagingSource {
        return getCharacterPagingSource(filterParameters.value).also {
            characterPagingSource = it
        }
    }

    fun refreshPagingData() {
        safeLaunch {
            characterPagingSource?.apply {
                if (networkState.value != ListNetworkState.Lost)
                    clearCache()
                invalidate()
            }
            if (networkStateFlow.value == ListNetworkState.Restored)
                networkStateFlow.tryEmit(ListNetworkState.Normal)
        }
    }

    val characterPagingData = Pager(
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