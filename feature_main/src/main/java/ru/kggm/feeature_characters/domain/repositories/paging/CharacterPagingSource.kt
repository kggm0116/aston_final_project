package ru.kggm.feeature_characters.domain.repositories.paging

import androidx.paging.PagingSource
import ru.kggm.feeature_characters.domain.entities.CharacterEntity

abstract class CharacterPagingSource: PagingSource<Int, CharacterEntity>() {
    abstract suspend fun invalidateCache()
}