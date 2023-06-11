package ru.kggm.feeature_main.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kggm.core.presentation.utility.safeLaunch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

class BlankFragmentViewModel: ViewModel() {
    private val testTextFlow = MutableStateFlow("")

    val testText = testTextFlow.asStateFlow()

    init {
        safeLaunch(Dispatchers.Default) {
            while (true) {
                testTextFlow.tryEmit(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                )
                delay(1000)
            }
        }
    }
}