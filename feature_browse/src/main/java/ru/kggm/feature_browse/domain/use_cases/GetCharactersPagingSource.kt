package ru.kggm.feature_browse.domain.use_cases

import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import ru.kggm.feature_browse.domain.paging.CharacterPagingFilters
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

typealias CharacterPagingSource = FilterPagingSource<CharacterEntity, CharacterPagingFilters>

class GetCharactersPagingSource @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(filterParameters: CharacterPagingFilters) : CharacterPagingSource =
        characterRepository.characterPagingSource(filterParameters)
}