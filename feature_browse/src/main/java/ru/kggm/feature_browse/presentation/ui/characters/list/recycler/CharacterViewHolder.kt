package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_main.databinding.PagerItemCharacterBinding

class CharacterViewHolder(
    private val binding: PagerItemCharacterBinding,
    private val onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var character: CharacterPresentationEntity

    fun bind(character: CharacterPresentationEntity) {
        this.character = character
        binding.root.setDebouncedClickListener { onCharacterClicked(character) }

        binding.textViewId.text = character.id.toString()
        binding.textViewName.text = character.name
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition() = absoluteAdapterPosition
            override fun getSelectionKey() = character.id
        }
}



