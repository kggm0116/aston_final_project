package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_main.databinding.PagerItemCharacterBinding

class CharacterPagingAdapter : PagingDataAdapter<CharacterPresentationEntity, CharacterViewHolder>(
    CharacterDiffUtil
) {

    var onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PagerItemCharacterBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(
            binding = binding,
            onCharacterClicked = onCharacterClicked,
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)!!
        holder.bind(character)
    }
}