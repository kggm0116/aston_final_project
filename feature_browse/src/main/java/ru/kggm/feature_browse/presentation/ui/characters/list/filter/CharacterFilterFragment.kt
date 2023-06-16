package ru.kggm.feature_browse.presentation.ui.characters.list.filter

import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.ViewModelBottomSheetDialogFragment
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListViewModel
import ru.kggm.feature_main.databinding.FragmentCharacterFilterBinding

class CharacterFilterFragment :
    ViewModelBottomSheetDialogFragment<FragmentCharacterFilterBinding, CharacterListViewModel>(
        CharacterListViewModel::class.java,
    ) {

    override fun createBinding() = FragmentCharacterFilterBinding.inflate(layoutInflater)
    override fun getViewModelOwner() = requireActivity()

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeViewListeners()
        lifecycleScope.launch { subscribeToViewModel() }
    }

    private fun initializeViewListeners() {
        binding.inputTextCharacterName.addTextChangedListener { onNameTextChanged(it!!) }
        binding.inputTextCharacterSpecies.addTextChangedListener { onSpeciesTextChanged(it!!) }
        binding.inputTextCharacterType.addTextChangedListener { onTypeTextChanged(it!!) }
        binding.buttonFilterCharacterGender.setOnClickListener { onGenderButtonClick() }
        binding.buttonFilterCharacterStatus.setOnClickListener { onStatusButtonClick() }
        binding.buttonApplyCharacterFilter.setOnClickListener { onApplyFiltersClick() }
    }

    private suspend fun subscribeToViewModel() {
        viewModel.filterParameters.collect { parameters ->
            with (parameters) {
                binding.buttonFilterCharacterGender.text = gender?.toString() ?: "Any"
                binding.buttonFilterCharacterStatus.text = status?.toString() ?: "Any"
                binding.inputTextCharacterName.setText(name)
                binding.inputTextCharacterSpecies.setText(species)
                binding.inputTextCharacterType.setText(type)
            }
        }
    }

    private fun onGenderButtonClick() {
        viewModel.cycleGender()
    }

    private fun onStatusButtonClick() {
        viewModel.cycleStatus()
    }

    private fun onApplyFiltersClick() {
        viewModel.applyFilters()
        this.dismiss()
    }

    private fun onNameTextChanged(editable: Editable) {
        viewModel.setNameFilter(editable.toString())
    }

    private fun onSpeciesTextChanged(editable: Editable) {
        viewModel.setSpeciesFilter(editable.toString())
    }

    private fun onTypeTextChanged(editable: Editable) {
        viewModel.setTypeFilter(editable.toString())
    }
}