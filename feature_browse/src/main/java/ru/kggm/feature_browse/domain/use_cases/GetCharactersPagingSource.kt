package ru.kggm.feature_browse.domain.use_cases

import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharactersPagingSource @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(filterParameters: CharacterFilterParameters) =
        characterRepository.characterPagingSource(filterParameters)
}