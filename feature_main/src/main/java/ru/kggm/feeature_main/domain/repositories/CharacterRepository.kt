package ru.kggm.feeature_main.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.kggm.feeature_main.domain.entities.CharacterEntity

interface CharacterRepository {
    suspend fun getCharacters(): List<CharacterEntity>
}