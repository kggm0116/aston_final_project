package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_main.databinding.RecyclerItemCharacterBinding
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import java.util.*

class CharactersListAdapter
    : PagingDataAdapter<CharacterPresentationEntity, CharacterViewHolder>(
    CharacterDiffUtil
) {
//    override fun submitList(list: List<CharacterPresentationEntity>?) {
//        runOnUiThread {
//            val bufferList = list?.toList()
//            super.submitList(bufferList)
//        }
//    }

    var onCharacterClicked: (CharacterPresentationEntity) -> Unit = { }

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
        val character = getItem(position)!!
        holder.bind(character)
    }

//    class KeyProvider(
//        recyclerView: RecyclerView
//    ) : ItemKeyProvider<Long>(SCOPE_MAPPED) {
//
//        private val adapter = recyclerView.adapter as CharactersListAdapter
//
//        override fun getKey(position: Int): Long? {
//            return adapter.getItem(position)?.id
//        }
//
//        override fun getPosition(key: Long): Int {
//            return adapter.currentList.indexOfFirst { it.id == key }
//        }
//    }

//    class DetailsLookup(private val recyclerView: RecyclerView
//    ) : ItemDetailsLookup<Long>() {
//        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
//            val view = recyclerView.findChildViewUnder(event.x, event.y)
//            if (view != null) {
//                val holder = recyclerView.getChildViewHolder(view) as CharacterViewHolder
//                return holder.getItemDetails()
//            }
//            return null
//        }
//    }
}