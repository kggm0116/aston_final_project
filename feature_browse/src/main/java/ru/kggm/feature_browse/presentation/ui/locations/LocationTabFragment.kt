package ru.kggm.feature_browse.presentation.ui.locations

import ru.kggm.core.presentation.ui.fragments.base.ViewBindingFragment
import ru.kggm.feature_main.databinding.FragmentLocationTabBinding

class LocationTabFragment : ViewBindingFragment<FragmentLocationTabBinding>() {
    override fun createBinding() = FragmentLocationTabBinding.inflate(layoutInflater)
}