package ru.kggm.feature_browse.presentation.ui.characters.filter

import android.content.DialogInterface
import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.bottom_sheet.ViewModelBottomSheetDialogFragment
import ru.kggm.core.presentation.utility.runOnUiThread
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListViewModel
import ru.kggm.feature_main.databinding.FragmentCharacterFilterBinding

class CharacterFilterFragment(private val onClosed: () -> Unit = { }) :
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
        subscribeToViewModel()
    }

    private fun initializeViewListeners() {
        binding.inputTextCharacterName.addTextChangedListener { onNameTextChanged(it!!) }
        binding.inputTextCharacterSpecies.addTextChangedListener { onSpeciesTextChanged(it!!) }
        binding.inputTextCharacterType.addTextChangedListener { onTypeTextChanged(it!!) }
        binding.buttonFilterCharacterGender.setDebouncedClickListener { onGenderButtonClick() }
        binding.buttonFilterCharacterStatus.setDebouncedClickListener { onStatusButtonClick() }
        binding.buttonApplyCharacterFilter.setDebouncedClickListener { onApplyFiltersClick() }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.filterParameters.collect { filters ->
                runOnUiThread { setFilters(filters) }
            }
        }
    }

    private fun setFilters(filters: CharacterPagingFilters) {
        binding.buttonFilterCharacterGender.text = filters.gender?.toString() ?: "Any"
        binding.buttonFilterCharacterStatus.text = filters.status?.toString() ?: "Any"
        binding.inputTextCharacterName.setTextKeepState(filters.nameQuery ?: "")
        binding.inputTextCharacterSpecies.setTextKeepState(filters.species ?: "")
        binding.inputTextCharacterType.setTextKeepState(filters.type ?: "")
    }

    private fun onGenderButtonClick() {
        viewModel.cycleGender()
    }

    private fun onStatusButtonClick() {
        viewModel.cycleStatus()
    }

    private fun onApplyFiltersClick() {
        viewModel.applyFilters()
        onClosed()
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

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClosed()
    }
}