package ru.kggm.feature_browse.domain.paging.filters

data class LocationPagingFilters(
    val nameQuery: String? = null,
    val type: String? = null,
    val dimension: String? = null,
) {
    companion object {
        val Default = LocationPagingFilters()
    }
}