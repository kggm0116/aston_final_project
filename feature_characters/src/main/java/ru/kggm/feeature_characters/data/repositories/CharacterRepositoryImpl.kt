package ru.kggm.feeature_characters.data.repositories

import ru.kggm.feeature_characters.data.network.services.CharacterService
import ru.kggm.feeature_characters.domain.entities.CharacterEntity
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterService: CharacterService
): CharacterRepository {
    override suspend fun getCharacters(): List<CharacterEntity> =
        characterService
            .getAllCharacters().join()
            .results
            .map {
                it.toDataEntity().toDomainEntity()
            }

    init {
        println()
    }
}