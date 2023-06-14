package ru.kggm.feature_browse.presentation.ui.characters

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
import ru.kggm.feature_browse.domain.use_cases.GetCharactersPagingSource
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val getCharactersPagingSource: GetCharactersPagingSource
): ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    val characterPagingData = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = {
            getCharactersPagingSource().also { source ->
                source.registerInvalidatedCallback {
                    safeLaunch { source.invalidateCache() }
                }
            }
        }
    )
        .flow
        .map { data ->
            data.map { it.toPresentationEntity() }
        }
        .cachedIn(viewModelScope)

    fun setDetailedCharacter(character: CharacterPresentationEntity) {
        detailedCharacterFlow.tryEmit(character)
    }

    private val detailedCharacterFlow = MutableStateFlow<CharacterPresentationEntity?>(null)
    val detailedCharacter = detailedCharacterFlow.asStateFlow()
}