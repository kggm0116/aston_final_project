package ru.kggm.feature_browse.domain.repositories.paging

import androidx.paging.PagingSource
import ru.kggm.feature_browse.domain.entities.CharacterEntity

abstract class CharacterPagingSource: PagingSource<Int, CharacterEntity>() {
    abstract suspend fun invalidateCache()
}