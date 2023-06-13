package ru.kggm.feeature_characters.domain.repositories

import androidx.paging.PagingSource
import ru.kggm.feeature_characters.domain.entities.CharacterEntity

interface CharacterRepository {
    fun characterPagingSource(): PagingSource<Int, CharacterEntity>
}