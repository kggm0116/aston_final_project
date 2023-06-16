package ru.kggm.core.presentation.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<VB : ViewBinding>(
    private val binding: VB
) : RecyclerView.ViewHolder(binding.root) {
    val context get() = binding.root.context!!
}