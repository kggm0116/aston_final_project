package ru.kggm.feature_browse.domain.paging

import androidx.paging.PagingSource
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters

abstract class BasePagingSource<T : Any>(
    val filterParameters: CharacterFilterParameters
): PagingSource<Int, T>() {

    abstract suspend fun clearCache()
}