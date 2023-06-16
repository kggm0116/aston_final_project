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
import kotlinx.coroutines.flow.onErrorResume
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.core.utility.classTagOf
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters
import ru.kggm.feature_browse.domain.entities.CharacterPagingSource
import ru.kggm.feature_browse.domain.use_cases.GetCharactersPagingSource
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class CharacterListViewModel @Inject constructor(
    private val getCharactersPagingSource: GetCharactersPagingSource
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    private val filterParametersFlow = MutableStateFlow(CharacterFilterParameters.Default)
    val filterParameters = filterParametersFlow.asStateFlow()

    fun cycleGender() {
        filterParametersFlow.value.run {
            copy(
                gender = when (gender) {
                    CharacterEntity.Gender.Female -> CharacterEntity.Gender.Male
                    CharacterEntity.Gender.Male -> CharacterEntity.Gender.Genderless
                    CharacterEntity.Gender.Genderless -> CharacterEntity.Gender.Unknown
                    CharacterEntity.Gender.Unknown -> null
                    null -> CharacterEntity.Gender.Female
                }
            ).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
                updateFilters()
            }
        }
    }

    fun cycleStatus() {
        filterParametersFlow.value.run {
            copy(
                status = when (status) {
                    CharacterEntity.Status.Alive -> CharacterEntity.Status.Dead
                    CharacterEntity.Status.Dead -> CharacterEntity.Status.Unknown
                    CharacterEntity.Status.Unknown -> null
                    null -> CharacterEntity.Status.Alive
                }
            ).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
                updateFilters()
            }
        }
    }

    fun setNameFilter(text: String) {
        filterParametersFlow.value.run {
            copy(
                name = text.ifEmpty { null }
            ).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
                updateFilters()
            }
        }
    }

    fun setSpeciesFilter(text: String) {
        filterParametersFlow.value.run {
            copy(
                species = text.ifEmpty { null }
            ).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
                updateFilters()
            }
        }
    }

    fun setTypeFilter(text: String) {
        filterParametersFlow.value.run {
            copy(
                type = text.ifEmpty { null }
            ).let { newParameters ->
                filterParametersFlow.tryEmit(newParameters)
                updateFilters()
            }
        }
    }


    private var characterPagingSource: CharacterPagingSource? = null
    private fun createPagingSource(): CharacterPagingSource {
        return getCharactersPagingSource(filterParametersFlow.value).also {
            if (characterPagingSource == null) safeLaunch { it.invalidateAndClearCache() }
            characterPagingSource = it
        }
    }

    private fun updateFilters() {
        safeLaunch { characterPagingSource?.invalidateAndClearCache() }
    }

    fun refreshPagingData() {
        safeLaunch { characterPagingSource?.invalidateAndClearCache() }
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
}