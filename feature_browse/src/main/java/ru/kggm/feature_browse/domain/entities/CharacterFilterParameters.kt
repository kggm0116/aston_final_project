package ru.kggm.feature_browse.domain.entities

data class CharacterFilterParameters(
    val name: String? = null,
    val status: CharacterEntity.Status? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: CharacterEntity.Gender? = null
) {
    companion object {
        val Default = CharacterFilterParameters()
    }
}