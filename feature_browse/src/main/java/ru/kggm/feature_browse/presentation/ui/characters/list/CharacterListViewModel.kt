package ru.kggm.feature_browse.presentation.ui.characters.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.entities.CharacterPagingSource
import ru.kggm.feature_browse.domain.use_cases.GetCharactersPagingSource
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class CharacterListViewModel @Inject constructor(
    private val getCharactersPagingSource: GetCharactersPagingSource
): ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    private lateinit var characterPagingSource: CharacterPagingSource
    private fun createPagingSource() = getCharactersPagingSource().apply {

    }

    fun refreshPagingData() = safeLaunch {
        characterPagingSource.invalidateCache()
    }

    val characterPagingData = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { createPagingSource() }
    )
        .flow
        .map { data ->
            data.map { it.toPresentationEntity() }
        }
        .cachedIn(viewModelScope)
}