package ru.kggm.feature_browse.domain.paging

import androidx.paging.PagingSource
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters

abstract class CharacterPagingSource(
    val filterParameters: CharacterFilterParameters
): PagingSource<Int, CharacterEntity>() {

    abstract suspend fun clearCache()
}