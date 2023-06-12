package ru.kggm.feeature_main.presentation.ui.characters.recycler

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.core.presentation.utility.runOnUiThread
import ru.kggm.feature_main.databinding.RecyclerItemCharacterBinding
import ru.kggm.feeature_main.presentation.entities.CharacterPresentationEntity
import java.util.*

class CharactersListAdapter
    : ListAdapter<CharacterPresentationEntity, CharacterViewHolder>(
    CharacterDiffUtil
) {
    override fun submitList(list: List<CharacterPresentationEntity>?) {
        runOnUiThread {
            val bufferList = list?.toList()
            super.submitList(bufferList)
        }
    }

    var onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }
    var selectionTracker: SelectionTracker<Long>? = null

    @Suppress("UNREACHABLE_CODE")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemCharacterBinding.inflate(
            inflater, parent, false
        )
        return CharacterViewHolder(
            binding = binding,
            onCharacterClicked = onCharacterClicked,
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    class KeyProvider(
        recyclerView: RecyclerView
    ) : ItemKeyProvider<Long>(SCOPE_MAPPED) {

        private val adapter = recyclerView.adapter as CharactersListAdapter

        override fun getKey(position: Int): Long? {
            return adapter.getItem(position)?.id
        }

        override fun getPosition(key: Long): Int {
            return adapter.currentList.indexOfFirst { it.id == key }
        }
    }

    class DetailsLookup(private val recyclerView: RecyclerView
    ) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                val holder = recyclerView.getChildViewHolder(view) as CharacterViewHolder
                return holder.getItemDetails()
            }
            return null
        }
    }
}