package ru.kggm.feature_browse.presentation.ui.episodes.filter

import android.content.DialogInterface
import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.bottom_sheet.ViewModelBottomSheetDialogFragment
import ru.kggm.core.presentation.utility.runOnUiThread
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.di.EpisodeComponent
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListViewModel
import ru.kggm.feature_main.databinding.FragmentEpisodeFilterBinding

class EpisodeFilterFragment(private val onClosed: () -> Unit = { }) :
    ViewModelBottomSheetDialogFragment<FragmentEpisodeFilterBinding, EpisodeListViewModel>(
        EpisodeListViewModel::class.java,
    ) {

    override fun createBinding() = FragmentEpisodeFilterBinding.inflate(layoutInflater)
    override fun getViewModelOwner() = requireActivity()

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        EpisodeComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeViewListeners()
        subscribeToViewModel()
    }

    private fun initializeViewListeners() {
        binding.inputTextEpisodeName.addTextChangedListener { onNameTextChanged(it!!) }
        binding.inputTextEpisodeCode.addTextChangedListener { onCodeTextChanged(it!!) }
        binding.buttonApplyEpisodeFilter.setDebouncedClickListener { onApplyFiltersClick() }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.filterParameters.collect { filters ->
                runOnUiThread { setFilters(filters) }
            }
        }
    }

    private fun setFilters(filters: EpisodePagingFilters) {
        binding.inputTextEpisodeName.setTextKeepState(filters.nameQuery ?: "")
        binding.inputTextEpisodeCode.setTextKeepState(filters.code ?: "")
    }

    private fun onApplyFiltersClick() {
        viewModel.applyFilters()
        onClosed()
        this.dismiss()
    }

    private fun onNameTextChanged(editable: Editable) {
        viewModel.setNameFilter(editable.toString())
    }

    private fun onCodeTextChanged(editable: Editable) {
        viewModel.setCodeFilter(editable.toString())
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClosed()
    }
}