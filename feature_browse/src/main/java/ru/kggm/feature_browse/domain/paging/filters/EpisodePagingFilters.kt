package ru.kggm.feature_browse.domain.paging.filters

data class EpisodePagingFilters(
    val nameQuery: String? = null,
    val code: String? = null,
) {
    companion object {
        val Default = EpisodePagingFilters()
    }
}