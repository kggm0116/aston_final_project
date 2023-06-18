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

package ru.kggm.core.presentation.ui.paging

import android.util.Log
import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.core.utility.classTag
import ru.kggm.presentation.databinding.PagerFooterLoadStateBinding

class CommonLoadStateViewHolder(
    private val binding: PagerFooterLoadStateBinding,
    private val onRetry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        Log.i(classTag(), "Load state: $loadState")
        when (loadState) {
            is LoadState.Loading -> {
                binding.layoutPagerFooterLoading.root.visibility = View.VISIBLE
                binding.layoutPagerFooterError.root.visibility = View.GONE
            }
            is LoadState.Error -> {
                binding.layoutPagerFooterLoading.root.visibility = View.GONE
                binding.layoutPagerFooterError.root.visibility = View.VISIBLE
            }
            is LoadState.NotLoading -> {
                binding.layoutPagerFooterLoading.root.visibility = View.GONE
                binding.layoutPagerFooterError.root.visibility = View.GONE
            }
        }
        binding.layoutPagerFooterError.buttonPagerFooterRetry.setOnClickListener { onRetry() }
    }
}
