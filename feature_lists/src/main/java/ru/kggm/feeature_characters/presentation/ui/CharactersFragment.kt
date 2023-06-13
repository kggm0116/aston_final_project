package ru.kggm.feeature_characters.presentation.ui

import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.BaseFragment
import ru.kggm.feature_main.databinding.FragmentCharactersBinding
import ru.kggm.feeature_characters.di.CharacterComponent
import ru.kggm.feeature_characters.presentation.entities.CharacterPresentationEntity
import ru.kggm.feeature_characters.presentation.ui.recycler.CharactersListAdapter

class CharactersFragment : BaseFragment<FragmentCharactersBinding, CharactersViewModel>(
    CharactersViewModel::class.java
) {
    override fun createBinding() = FragmentCharactersBinding.inflate(layoutInflater)
    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeRecycler()
        binding.refresherCharacters.setOnRefreshListener { onRefreshRecycler() }
        lifecycleScope.launch { subscribeToViewModel() }
    }

    private val recyclerAdapter = CharactersListAdapter()
    private fun initializeRecycler() {
        binding.recyclerCharacters.adapter = recyclerAdapter
        recyclerAdapter.onCharacterClicked = { onCharacterClicked(it) }
    }

    private suspend fun subscribeToViewModel() {
        viewModel.characterPagingData.collect {
            recyclerAdapter.submitData(it)
        }
    }

    private fun onCharacterClicked(character: CharacterPresentationEntity) {

    }

    private fun onRefreshRecycler() {
        recyclerAdapter.refresh()
        binding.refresherCharacters.isRefreshing = false
    }
}