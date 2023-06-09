package ru.kggm.core.presentation.ui.fragments.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import ru.kggm.core.application.DependenciesProviderApplication
import ru.kggm.core.di.DependenciesProvider
import javax.inject.Inject

abstract class ViewModelFragment<VB : ViewBinding, VM : ViewModel>(
    private val viewModelClass: Class<VM>,
) : ViewBindingFragment<VB>() {

    private var _viewModel: VM? = null
    protected val viewModel get() = requireNotNull(_viewModel) { "ViewModel not initialized" }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract fun viewModelOwner(): ViewModelStoreOwner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDaggerComponent(
            (requireActivity().applicationContext as DependenciesProviderApplication)
                .getDependenciesProvider()
        )
        _viewModel = ViewModelProvider(viewModelOwner(), viewModelFactory)[viewModelClass]
    }

    abstract fun initDaggerComponent(dependenciesProvider: DependenciesProvider)
}