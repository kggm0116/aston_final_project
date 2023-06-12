package ru.kggm.feeature_main.presentation.ui.characters.recycler

import android.util.TypedValue
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_main.databinding.RecyclerItemCharacterBinding
import ru.kggm.feeature_main.presentation.entities.CharacterPresentationEntity

class CharacterViewHolder(
    val binding: RecyclerItemCharacterBinding,
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



