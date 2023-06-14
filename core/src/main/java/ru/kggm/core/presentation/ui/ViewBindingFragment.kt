package ru.kggm.core.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import ru.kggm.core.application.DependenciesProviderApplication
import ru.kggm.core.di.DependenciesProvider
import javax.inject.Inject

abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding) { "Binding not initialized" }

    abstract fun onInitialize()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = createBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitialize()
    }

    abstract fun createBinding(): VB
}