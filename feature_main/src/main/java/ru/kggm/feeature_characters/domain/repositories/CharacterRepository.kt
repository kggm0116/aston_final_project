package ru.kggm.feeature_characters.domain.repositories

import androidx.paging.PagingSource
import ru.kggm.feeature_characters.domain.entities.CharacterEntity
import ru.kggm.feeature_characters.domain.repositories.paging.CharacterPagingSource

interface CharacterRepository {
    fun characterPagingSource(): CharacterPagingSource
}