package ru.kggm.feature_browse.presentation.ui.characters

import ru.kggm.core.presentation.ui.fragments.fragment.ViewBindingFragment
import ru.kggm.feature_main.databinding.NewFragmentCharacterTabBinding

class CharacterTabFragment : ViewBindingFragment<NewFragmentCharacterTabBinding>() {
    override fun createBinding() = NewFragmentCharacterTabBinding.inflate(layoutInflater)
}