package ru.kggm.feature_browse.presentation.ui.shared

interface ItemClickable<T> {
    var onItemClicked: (T) -> Unit
}