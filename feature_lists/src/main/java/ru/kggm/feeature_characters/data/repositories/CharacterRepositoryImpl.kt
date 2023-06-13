package ru.kggm.feeature_characters.data.repositories

import androidx.paging.PagingSource
import ru.kggm.feeature_characters.data.network.services.CharacterService
import ru.kggm.feeature_characters.data.paging.CharacterPagingSourceImpl
import ru.kggm.feeature_characters.domain.entities.CharacterEntity
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterService: CharacterService
): CharacterRepository {
    override fun characterPagingSource(): PagingSource<Int, CharacterEntity> =
        CharacterPagingSourceImpl(characterService)
}