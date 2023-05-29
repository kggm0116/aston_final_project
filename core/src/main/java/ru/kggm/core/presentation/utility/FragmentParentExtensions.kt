package ru.kggm.core.presentation.utility

import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

inline fun <reified T : Fragment> Fragment.parentFragmentOfType(): T {
    var parent: Fragment?
    while (true) {
        parent = parentFragment
            ?: throw IllegalStateException("Could not find parent of type ${T::class.simpleName}")
        if (parent is T)
            return parent
    }
}