package ru.kggm.feature_browse.presentation.ui.characters.list

import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.ViewModelBottomSheetDialogFragment
import ru.kggm.core.presentation.utility.parentFragmentOfType
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.ui.characters.CharactersFragment
import ru.kggm.feature_main.databinding.FragmentCharacterFilterBinding

class CharacterFilterFragment :
    ViewModelBottomSheetDialogFragment<FragmentCharacterFilterBinding, CharacterListViewModel>(
        CharacterListViewModel::class.java,
    ) {
    override fun createBinding() = FragmentCharacterFilterBinding.inflate(layoutInflater)
    override fun getViewModelOwner() = parentFragmentOfType<CharactersFragment>()

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
    }

//    private val searchStringQueryListener =
//        object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let {
//                    viewModel.searchString = it
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let {
//                    viewModel.searchString = it
//                }
//                return true
//            }
//        }

    private fun onGenderButtonClick() {
        viewModel.cycleGender()
    }

    private fun onStatusButtonClick() {
        viewModel.cycleStatus()
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

    private suspend fun subscribeToViewModel() {
        viewModel.filterParameters.collect { parameters ->
            binding.buttonFilterCharacterGender.text = parameters.gender?.toString() ?: "Any"
            binding.buttonFilterCharacterStatus.text = parameters.status?.toString() ?: "Any"
        }
    }
}