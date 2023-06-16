package ru.kggm.feature_browse.domain.entities

import androidx.paging.PagingSource

abstract class CharacterPagingSource(
    val filterParameters: CharacterFilterParameters
): PagingSource<Int, CharacterEntity>() {

    abstract suspend fun clearCache()

    class CharacterPagerLoadError: Exception()
}