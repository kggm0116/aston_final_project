package ru.kggm.feature_browse.presentation.ui.utility

import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.LocationEntity
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters

val genericCharacterEntity = CharacterEntity(
    id = 0,
    name = "name",
    status = CharacterEntity.Status.Alive,
    species = "species",
    type = "type",
    gender = CharacterEntity.Gender.Male,
    image = "image",
    locationId = 1,
    originId = 1,
    episodeIds = listOf(1, 2, 3)
)

val genericLocationEntity = LocationEntity(
    id = 0,
    name = "name",
    type = "type",
    dimension = "dimension",
    residentIds = listOf(1, 2, 3)
)