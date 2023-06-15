package ru.kggm.feature_browse.domain.use_cases

import ru.kggm.feature_browse.domain.entities.CharacterFilterCollection
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharactersPagingSource @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(
        searchString: String = "",
        filters: CharacterFilterCollection = CharacterFilterCollection.Empty
    ) = characterRepository.characterPagingSource(searchString, filters)
}