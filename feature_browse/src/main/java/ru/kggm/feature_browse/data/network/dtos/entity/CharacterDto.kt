package ru.kggm.feature_browse.data.network.dtos.entity

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.network.services.EpisodeService.Companion.getEpisodeId
import ru.kggm.feature_browse.data.network.services.LocationService.Companion.getLocationId
import ru.kggm.feature_browse.domain.entities.CharacterEntity

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val type: String,
    val gender: Gender,
    val image: String,
    val origin: LocationShortDto,
    val location: LocationShortDto,
    @SerializedName("episode")
    val episodeUrls: List<String>
) {
    fun toDataEntity() = CharacterDataEntity(
        id = id,
        name = name,
        status = status.toDomainStatus(),
        species = species,
        type = type,
        gender = gender.toDomainStatus(),
        image = image,
        originId = origin.url.takeIf { it.isNotEmpty() }?.getLocationId(),
        locationId = location.url.takeIf { it.isNotEmpty() }?.getLocationId(),
        episodeIds = episodeUrls.map { it.getEpisodeId() }
    )

    enum class Status { Alive, Dead, @SerializedName("unknown") Unknown }
    enum class Gender { Female, Male, Genderless, @SerializedName("unknown") Unknown }

    companion object {

        fun Status.toDomainStatus() = when(this) {
            Status.Alive -> CharacterEntity.Status.Alive
            Status.Dead -> CharacterEntity.Status.Dead
            Status.Unknown -> CharacterEntity.Status.Unknown
        }

        fun Gender.toDomainStatus() = when(this) {
            Gender.Female -> CharacterEntity.Gender.Female
            Gender.Male -> CharacterEntity.Gender.Male
            Gender.Genderless -> CharacterEntity.Gender.Genderless
            Gender.Unknown -> CharacterEntity.Gender.Unknown
        }
    }
}