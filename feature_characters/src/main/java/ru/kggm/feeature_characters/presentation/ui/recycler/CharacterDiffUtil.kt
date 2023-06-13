package ru.kggm.feeature_characters.presentation.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.kggm.feeature_characters.presentation.entities.CharacterPresentationEntity

object CharacterDiffUtil : DiffUtil.ItemCallback<CharacterPresentationEntity>() {
    override fun areItemsTheSame(
        oldItem: CharacterPresentationEntity,
        newItem: CharacterPresentationEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharacterPresentationEntity,
        newItem: CharacterPresentationEntity
    ): Boolean {
        return oldItem == newItem
    }
}