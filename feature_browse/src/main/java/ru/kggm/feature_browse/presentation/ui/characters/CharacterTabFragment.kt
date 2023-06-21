package ru.kggm.feature_browse.presentation.ui.characters

import ru.kggm.core.presentation.ui.fragments.base.ViewBindingFragment
import ru.kggm.feature_main.databinding.FragmentCharacterTabBinding

class CharacterTabFragment : ViewBindingFragment<FragmentCharacterTabBinding>() {
    override fun createBinding() = FragmentCharacterTabBinding.inflate(layoutInflater)
}