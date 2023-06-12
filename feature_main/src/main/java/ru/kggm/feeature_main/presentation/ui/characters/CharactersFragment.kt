package ru.kggm.feeature_main.presentation.ui.characters

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import kotlinx.coroutines.launch
import ru.kggm.core.presentation.base.BaseFragment
import ru.kggm.feature_main.databinding.FragmentCharactersBinding
import ru.kggm.feeature_main.presentation.entities.CharacterPresentationEntity
import ru.kggm.feeature_main.presentation.ui.characters.recycler.CharactersListAdapter

class CharactersFragment : BaseFragment<FragmentCharactersBinding, CharactersViewModel>(
    CharactersViewModel::class.java
) {
    override fun createBinding() = FragmentCharactersBinding.inflate(layoutInflater)
    private val viewModel: CharactersViewModel by activityViewModels()



    override fun onInitialize() {
        initalizeRecycler()
        binding.refresherCharacters.setOnRefreshListener { onRefreshRecycler() }
        lifecycleScope.launch {
            viewModel.characters.collect { characters ->
                recyclerAdapter.submitList(characters)
            }
        }
    }

    private val recyclerAdapter = CharactersListAdapter()
    lateinit var selectionTracker: SelectionTracker<Long>
    private fun initalizeRecycler() {
        binding.recyclerCharacters.adapter = recyclerAdapter
        selectionTracker = SelectionTracker.Builder(
            "charactersSelectionTracker",
            binding.recyclerCharacters,
            CharactersListAdapter.KeyProvider(binding.recyclerCharacters),
            CharactersListAdapter.DetailsLookup(binding.recyclerCharacters),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        recyclerAdapter.selectionTracker = selectionTracker
        recyclerAdapter.onCharacterClicked = { onCharacterClicked(it) }
    }

    private fun onCharacterClicked(character: CharacterPresentationEntity) {

    }

    private fun onRefreshRecycler() {
        viewModel.loadCharacters()
        binding.refresherCharacters.isRefreshing = false
    }
}