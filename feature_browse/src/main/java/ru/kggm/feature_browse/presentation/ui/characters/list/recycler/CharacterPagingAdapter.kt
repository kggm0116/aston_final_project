package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.ItemClickable
import ru.kggm.feature_browse.databinding.LayoutCharacterItemBinding

class CharacterPagingAdapter : PagingDataAdapter<CharacterPresentationEntity, CharacterViewHolder>(
    CharacterDiffUtil
), ItemClickable<CharacterPresentationEntity> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutCharacterItemBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(
            binding = binding,
            onCharacterClicked = { onItemClicked(it) },
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)!!
        holder.bind(character)
    }

    override var onItemClicked: (CharacterPresentationEntity) -> Unit = { }
}