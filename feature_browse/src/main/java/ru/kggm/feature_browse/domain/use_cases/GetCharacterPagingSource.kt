package ru.kggm.feature_browse.domain.use_cases

import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters
import ru.kggm.feature_browse.domain.repositories.CharacterPagingSource
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharacterPagingSource @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(filters: CharacterPagingFilters) : CharacterPagingSource =
        characterRepository.pagingSource(filters)
}