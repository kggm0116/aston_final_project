package ru.kggm.feature_browse.presentation.ui.locations.details

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kggm.core.presentation.utility.safeLaunch
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity.Companion.toPresentationEntity
import javax.inject.Inject

class LocationDetailsViewModel @Inject constructor(
    private val getLocationById: GetLocationById
): ViewModel() {

    init {
        Log.i(classTag(), "Initialized")
    }

    override fun onCleared() {
        Log.i(classTag(), "Cleared")
        super.onCleared()
    }

    fun loadLocation(id: Long) = safeLaunch {
        getLocationById(id.toInt()).let {
            locationFlow.tryEmit(it.toPresentationEntity())
        }
    }

    private val locationFlow = MutableStateFlow<LocationPresentationEntity?>(null)
    val location = locationFlow.asStateFlow()
}