package ru.kggm.feeature_main.presentation.ui.main

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.presentation.base.BaseFragment
import ru.kggm.feature_main.databinding.FragmentBlankBinding

class BlankFragment : BaseFragment<FragmentBlankBinding, BlankFragmentViewModel>(
    BlankFragmentViewModel::class.java
) {
    override fun createBinding() = FragmentBlankBinding.inflate(layoutInflater)
    private val viewModel: BlankFragmentViewModel by activityViewModels()

    override fun onInitialize() {
        lifecycleScope.launch {
            viewModel.testText.collect { text ->
                binding.textViewTest.text = text
            }
        }
    }
}