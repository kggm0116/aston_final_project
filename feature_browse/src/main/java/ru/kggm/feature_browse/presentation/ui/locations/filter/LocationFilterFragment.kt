package ru.kggm.feature_browse.presentation.ui.locations.filter

import android.content.DialogInterface
import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.bottom_sheet.ViewModelBottomSheetDialogFragment
import ru.kggm.core.presentation.utility.runOnUiThread
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.di.LocationComponent
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters
import ru.kggm.feature_browse.presentation.ui.locations.list.LocationListViewModel
import ru.kggm.feature_main.databinding.FragmentLocationFilterBinding

class LocationFilterFragment(private val onClosed: () -> Unit = { }) :
    ViewModelBottomSheetDialogFragment<FragmentLocationFilterBinding, LocationListViewModel>(
        LocationListViewModel::class.java,
    ) {

    override fun createBinding() = FragmentLocationFilterBinding.inflate(layoutInflater)
    override fun getViewModelOwner() = requireActivity()

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        LocationComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeViewListeners()
        subscribeToViewModel()
    }

    private fun initializeViewListeners() {
        binding.inputTextLocationName.addTextChangedListener { onNameTextChanged(it!!) }
        binding.inputTextLocationType.addTextChangedListener { onTypeTextChanged(it!!) }
        binding.inputTextLocationDimension.addTextChangedListener { onDimensionTextChanged(it!!) }
        binding.buttonApplyLocationFilter.setDebouncedClickListener { onApplyFiltersClick() }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.filterParameters.collect { filters ->
                runOnUiThread { setFilters(filters) }
            }
        }
    }

    private fun setFilters(filters: LocationPagingFilters) {
        binding.inputTextLocationName.setTextKeepState(filters.nameQuery ?: "")
        binding.inputTextLocationType.setTextKeepState(filters.type ?: "")
        binding.inputTextLocationDimension.setTextKeepState(filters.dimension ?: "")
    }

    private fun onApplyFiltersClick() {
        viewModel.applyFilters()
        onClosed()
        this.dismiss()
    }

    private fun onNameTextChanged(editable: Editable) {
        viewModel.setNameFilter(editable.toString())
    }

    private fun onDimensionTextChanged(editable: Editable) {
        viewModel.setDimensionFilter(editable.toString())
    }

    private fun onTypeTextChanged(editable: Editable) {
        viewModel.setTypeFilter(editable.toString())
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClosed()
    }
}