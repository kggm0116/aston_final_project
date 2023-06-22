package ru.kggm.feature_browse.presentation.ui.shared

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.kggm.presentation.R

fun Fragment.openDetailsFragment(fragment: Fragment) {
    requireActivity().supportFragmentManager.commit {
        setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        replace(ru.kggm.feature_main.R.id.fragment_container_browse, fragment)
        addToBackStack(null)
    }
}