package ru.kggm.feeature_characters.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feeature_characters.domain.entities.CharacterEntity
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetAllCharacters @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): List<CharacterEntity> = withContext(Dispatchers.IO) {
        characterRepository.getCharacters()
    }
}