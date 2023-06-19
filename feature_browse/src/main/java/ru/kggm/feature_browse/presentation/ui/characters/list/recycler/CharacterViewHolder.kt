package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import androidx.core.view.isVisible
import coil.load
import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
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
            binding.layoutPagerItemCharacter.textViewCharacterName.text = name
            binding.layoutPagerItemCharacter.textViewCharacterType.text = type
            binding.layoutPagerItemCharacter.textViewCharacterSpecies.text = species
            binding.layoutPagerItemCharacter.textViewCharacterStatus.text = status.toResourceString(context)
            binding.layoutPagerItemCharacter.textViewCharacterGender.text = gender.toResourceString(context)

            binding.layoutPagerItemCharacter.textViewCharacterType.isVisible = type.isNotEmpty()
            binding.layoutPagerItemCharacter.textViewCharacterSpecies.isVisible = species.isNotEmpty()
        }
    }
}



