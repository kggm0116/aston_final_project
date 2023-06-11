package ru.kggm.core.presentation.utility.activity_result_managers

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PickMediaManager(fragment: Fragment) {
    private var onPicked: (Uri) -> Unit = { }
    private val launcher = fragment.registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { result ->
        result?.let { uri -> onPicked(uri) }
    }

    fun pickMedia(
        type: ActivityResultContracts.PickVisualMedia.VisualMediaType,
        onPicked: (Uri) -> Unit
    ) {
        this.onPicked = onPicked
        launcher.launch(PickVisualMediaRequest(type))
    }
}