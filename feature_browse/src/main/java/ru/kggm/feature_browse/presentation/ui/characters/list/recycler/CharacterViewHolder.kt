package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import androidx.core.view.isVisible
import coil.load
import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.PagerItemCharacterBinding

class CharacterViewHolder(
    private val binding: PagerItemCharacterBinding,
    private val onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }
) : BaseViewHolder<PagerItemCharacterBinding>(binding) {

    lateinit var character: CharacterPresentationEntity

    fun bind(character: CharacterPresentationEntity) {
        this.character = character
        binding.root.setDebouncedClickListener { onCharacterClicked(character) }
        displayCharacter()
    }
    
    private fun displayCharacter() {
        with (character) {
            binding.imageCharacter.load(image) { crossfade(true) }
            binding.textViewCharacterType.text = type
            binding.textViewCharacterSpecies.text = species
            binding.textViewCharacterStatus.text = status.toResourceString(context)
            binding.textViewCharacterGender.text = gender.toResourceString(context)

            binding.textViewCharacterType.isVisible = type.isNotEmpty()
            binding.textViewCharacterSpecies.isVisible = species.isNotEmpty()
        }
    }
}



