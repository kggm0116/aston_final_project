package ru.kggm.feature_browse.presentation.ui.characters.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.feature_browse.domain.use_cases.GetCharacterById
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterById: GetCharacterById,
    private val getLocationById: GetLocationById,
): ViewModel() {

    fun loadCharacter(id: Long) = safeLaunch {
        if (characterFlow.value.item?.id == id)
            return@safeLaunch

        characterFlow.tryEmit(LoadResult(null, LoadingState.Loading))
        when (val result = getCharacterById(id.toInt())) {
            null -> {
                characterFlow.tryEmit(LoadResult(null, LoadingState.Error))
            }
            else -> {
                val character = result.toPresentationEntity()
                characterFlow.tryEmit(
                    LoadResult(character, LoadingState.Loaded)
                )
                loadOrigin(character)
                loadLocation(character)
            }
        }
    }

    private fun loadOrigin(character: CharacterPresentationEntity) {
        if (character.originId == null) {
            originFlow.tryEmit(LoadResult(null, LoadingState.Loaded))
        } else {
            safeLaunch {
                originFlow.tryEmit(LoadResult(null, LoadingState.Loading))
                when (val result = getLocationById(character.originId)) {
                    null -> originFlow.tryEmit(LoadResult(null, LoadingState.Error))
                    else -> {
                        originFlow.tryEmit(
                            LoadResult(result.toPresentationEntity(), LoadingState.Loaded)
                        )
                    }
                }
            }
        }
    }

    private fun loadLocation(character: CharacterPresentationEntity) {
        if (character.locationId == null) {
            locationFlow.tryEmit(LoadResult(null, LoadingState.Loaded))
        } else {
            safeLaunch {
                locationFlow.tryEmit(LoadResult(null, LoadingState.Loading))
                when (val result = getLocationById(character.locationId)) {
                    null -> locationFlow.tryEmit(LoadResult(null, LoadingState.Error))
                    else -> {
                        locationFlow.tryEmit(
                            LoadResult(result.toPresentationEntity(), LoadingState.Loaded)
                        )
                    }
                }
            }
        }
    }

    private val characterFlow = MutableStateFlow(
        LoadResult<CharacterPresentationEntity?>(null, LoadingState.Loading)
    )
    val character = characterFlow.asStateFlow()

    private val originFlow = MutableStateFlow(
        LoadResult<LocationPresentationEntity?>(null, LoadingState.Loading)
    )
    val origin = originFlow.asStateFlow()

    private val locationFlow = MutableStateFlow(
        LoadResult<LocationPresentationEntity?>(null, LoadingState.Loading)
    )
    val location = locationFlow.asStateFlow()
}