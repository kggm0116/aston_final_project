package ru.kggm.feeature_characters.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feeature_characters.domain.entities.CharacterEntity
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharactersPagingSource @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke() = characterRepository.characterPagingSource()
}