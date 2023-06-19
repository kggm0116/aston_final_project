package ru.kggm.feature_browse.presentation.ui.characters.recycler

import androidx.core.view.isVisible
import coil.load
import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_main.databinding.NewItemCharacterBinding
import ru.kggm.feature_main.databinding.PagerItemCharacterBinding

class CharacterViewHolder(
    private val binding: NewItemCharacterBinding,
    private val onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }
) : BaseViewHolder<NewItemCharacterBinding>(binding) {

    lateinit var character: CharacterPresentationEntity

    fun bind(character: CharacterPresentationEntity) {
        this.character = character
        binding.root.setDebouncedClickListener { onCharacterClicked(character) }
        displayCharacter()
    }
    
    private fun displayCharacter() {
        with (character) {
            binding.imageCharacter.load(image) { crossfade(true) }
            binding.newLayoutCharacterInfoShort.textViewCharacterName.text = name
            binding.newLayoutCharacterInfoShort.textViewCharacterSpecies.text = species
            binding.newLayoutCharacterInfoShort.textViewCharacterStatus.text = status.toResourceString(context)
            binding.newLayoutCharacterInfoShort.textViewCharacterGender.text = gender.toResourceString(context)

            binding.newLayoutCharacterInfoShort.textViewCharacterSpecies.isVisible = species.isNotEmpty()
        }
    }
}



