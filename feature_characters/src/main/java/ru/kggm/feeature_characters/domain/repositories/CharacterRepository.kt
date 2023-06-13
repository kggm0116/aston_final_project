package ru.kggm.feeature_characters.domain.repositories

import ru.kggm.feeature_characters.domain.entities.CharacterEntity

interface CharacterRepository {
    suspend fun getCharacters(): List<CharacterEntity>
}