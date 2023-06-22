package ru.kggm.feature_browse.presentation.ui.locations.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import javax.inject.Inject

class LocationDetailsViewModel @Inject constructor(
    private val getLocationById: GetLocationById
): ViewModel() {

    fun loadLocation(id: Long) = safeLaunch {
        locationFlow.tryEmit(LoadResult(null, LoadingState.Loading))
        when (val result = getLocationById(id.toInt())) {
            null -> locationFlow.tryEmit(LoadResult(null, LoadingState.Error))
            else -> {
                val location = result.toPresentationEntity()
                locationFlow.tryEmit(
                    LoadResult(location, LoadingState.Loaded)
                )
            }
        }
    }

    private val locationFlow = MutableStateFlow(
        LoadResult<LocationPresentationEntity?>(null, LoadingState.Loading)
    )
    val location = locationFlow.asStateFlow()
}