package ru.kggm.feeature_characters.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import ru.kggm.feeature_characters.domain.use_cases.GetCharactersPagingSource
import ru.kggm.feeature_characters.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val getCharactersPagingSource: GetCharactersPagingSource
): ViewModel() {

    companion object {
        private const val PAGE_SIZE = 50
    }

    val characterPagingData = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { getCharactersPagingSource() }
    )
        .flow
        .map { data ->
            data.map { it.toPresentationEntity() }
        }
        .cachedIn(viewModelScope)

    fun resetPaging() {
        getCharactersPagingSource().invalidate()
    }
}