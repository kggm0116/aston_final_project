package ru.kggm.feeature_main.data.repositories

import ru.kggm.feeature_main.data.network.Network
import ru.kggm.feeature_main.domain.entities.CharacterEntity
import ru.kggm.feeature_main.domain.repositories.CharacterRepository

class CharacterRepositoryImpl: CharacterRepository {
    override suspend fun getCharacters(): List<CharacterEntity> =
        Network.characterService
            .getAllCharacters().join()
            .results
            .map {
                it.toDataEntity().toDomainEntity()
            }
}