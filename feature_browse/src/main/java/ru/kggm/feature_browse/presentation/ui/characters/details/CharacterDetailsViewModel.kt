package ru.kggm.feature_browse.presentation.ui.characters.details

import androidx.lifecycle.ViewModel
import ru.kggm.feature_browse.domain.use_cases.GetCharacterById
import ru.kggm.feature_browse.domain.use_cases.GetCharactersPagingSource
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterById: GetCharacterById,
    private val getCharactersPagingSource: GetCharactersPagingSource,
): ViewModel() {
//    companion object {
//        const val ARG_DETAILED_CHARACTER_ID = "ARG_DETAILED_CHARACTER"
//    }
//    val id by lazy {
//        savedStateHandle.get<Int>(ARG_DETAILED_CHARACTER_ID)
//            ?: throw IllegalStateException("No detailed character arg passed to ViewModel")
//    }
//    init {
//        safeLaunch {
//            getCharacterById(id).let {
//                _character.value = it.toPresentationEntity()
//            }
//        }
//    }
//    val _character = MutableLiveData<CharacterPresentationEntity?>(null)
//    val character: LiveData<CharacterPresentationEntity?> = _character
}