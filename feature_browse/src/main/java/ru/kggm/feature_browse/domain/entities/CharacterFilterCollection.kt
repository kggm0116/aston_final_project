package ru.kggm.feature_browse.domain.entities

class CharacterFilterCollection(
    private val filters: List<CharacterFilter>,
    override val size: Int = filters.size
): Collection<CharacterFilter> {

    init {
        require(filters.groupBy { it::class }.none { it.value.size > 1 }) {
            "CharacterFilterCollection must contain at most one of each filter type"
        }
    }

    companion object {
        val Empty = CharacterFilterCollection(emptyList())
    }

    override fun contains(element: CharacterFilter) = filters.contains(element)

    override fun containsAll(elements: Collection<CharacterFilter>) = filters.containsAll(elements)

    override fun isEmpty() = filters.isEmpty()

    override fun iterator() = filters.iterator()
}