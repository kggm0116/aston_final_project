package ru.kggm.feeature_main.presentation.ui.characters

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.feeature_main.domain.use_cases.GetAllCharacters
import ru.kggm.feeature_main.presentation.entities.CharacterPresentationEntity
import ru.kggm.feeature_main.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity

class CharactersViewModel(
    private val getAllCharacters: GetAllCharacters = GetAllCharacters()
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