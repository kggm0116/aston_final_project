package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import coil.load
import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.R
import ru.kggm.feature_browse.databinding.LayoutCharacterItemBinding
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString

class CharacterViewHolder(
    private val binding: LayoutCharacterItemBinding,
    private val onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }
) : BaseViewHolder<LayoutCharacterItemBinding>(binding) {

    private lateinit var character: CharacterPresentationEntity

    fun bind(character: CharacterPresentationEntity) {
        this.character = character
        binding.root.setDebouncedClickListener { onCharacterClicked(character) }
        displayCharacter()
    }

    private fun displayCharacter() {
        with (character) {
            binding.image.load(image) { crossfade(true) }
            binding.info.textViewName.text = name
            binding.info.textViewStatus.text = context.getString(
                R.string.composite_text_character_status,
                status.toResourceString(context)
            )
            binding.info.textViewSpecies.text = context.getString(
                R.string.composite_text_character_species,
                species
            )
            binding.info.textViewGender.text  = context.getString(
                R.string.composite_text_character_gender,
                gender.toResourceString(context)
            )
        }
    }
}


