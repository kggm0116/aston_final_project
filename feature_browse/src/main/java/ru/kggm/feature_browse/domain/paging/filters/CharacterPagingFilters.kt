package ru.kggm.feature_browse.domain.paging.filters

import ru.kggm.feature_browse.domain.entities.CharacterEntity

data class CharacterPagingFilters(
    val nameQuery: String? = null,
    val status: CharacterEntity.Status? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: CharacterEntity.Gender? = null
) {
    companion object {
        val Default = CharacterPagingFilters()
    }
}