package ru.kggm.feature_browse.domain.paging

import androidx.paging.PagingSource

abstract class FilterPagingSource<TOut : Any, TFilter : Any>(
    val filters: TFilter
): PagingSource<Int, TOut>() {
    abstract suspend fun clearCache()
}