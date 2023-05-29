package ru.kggm.feature_browse.presentation.ui.shared

data class LoadResult<T>(
    val item: T,
    val state: LoadingState
)
