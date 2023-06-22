package ru.kggm.feature_browse.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharacterById @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        characterRepository.getById(id)
    }
}