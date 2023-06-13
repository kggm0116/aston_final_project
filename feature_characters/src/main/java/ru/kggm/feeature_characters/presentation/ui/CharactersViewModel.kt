package ru.kggm.feeature_characters.presentation.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.feeature_characters.domain.use_cases.GetAllCharacters
import ru.kggm.feeature_characters.presentation.entities.CharacterPresentationEntity
import ru.kggm.feeature_characters.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val getAllCharacters: GetAllCharacters
): ViewModel() {

    init {
        loadCharacters()
    }

    private val charactersFlow = MutableStateFlow(emptyList<CharacterPresentationEntity>())
    val characters = charactersFlow.asStateFlow()

    fun loadCharacters() {
        safeLaunch {
            getAllCharacters()
                .map { it.toPresentationEntity() }
                .let { charactersFlow.emit(it) }
        }
    }
}