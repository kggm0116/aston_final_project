package ru.kggm.feeature_main.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.kggm.feeature_main.data.repositories.CharacterRepositoryImpl
import ru.kggm.feeature_main.domain.entities.CharacterEntity
import ru.kggm.feeature_main.domain.repositories.CharacterRepository

class GetAllCharacters(
    private val characterRepository: CharacterRepository = CharacterRepositoryImpl()
) {
    suspend operator fun invoke(): List<CharacterEntity> = withContext(Dispatchers.IO) {
        characterRepository.getCharacters()
    }
}