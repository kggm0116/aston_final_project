/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import android.util.Log
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.core.utility.classTag
import ru.kggm.feature_main.databinding.PagerFooterCharacterLoadStateBinding

class CharacterLoadStateViewHolder(
    private val binding: PagerFooterCharacterLoadStateBinding,
    private val onRetry: () -> Unit = { }
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        Log.i(classTag(), "Load state: $loadState")
//        if (loadState is LoadState.Error) {
//            binding.errorMsg.text = loadState.error.localizedMessage
//        }
//        binding.progressBar.isVisible = loadState is LoadState.Loading
//        binding.retryButton.isVisible = loadState is LoadState.Error
//        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

//    companion object {
//        fun create(parent: ViewGroup, retry: () -> Unit): CharacterLoadStateViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.repos_load_state_footer_view_item, parent, false)
//            val binding = ReposLoadStateFooterViewItemBinding.bind(view)
//            return CharacterLoadStateViewHolder(binding, retry)
//        }
//    }
}
