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
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.use_cases.GetCharacterById
import ru.kggm.feature_browse.domain.use_cases.GetCharactersPagingSource
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterById: GetCharacterById
): ViewModel() {

    init {
        Log.i(classTag(), "Initialized")
    }

    override fun onCleared() {
        Log.i(classTag(), "Cleared")
        super.onCleared()
    }

    fun loadCharacter(id: Long) = safeLaunch {
        getCharacterById(id).let {
            characterFlow.tryEmit(it.toPresentationEntity())
        }
    }

    private val characterFlow = MutableStateFlow<CharacterPresentationEntity?>(null)
    val character = characterFlow.asStateFlow()
}