package ru.kggm.feature_browse.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListFragment
import ru.kggm.feature_browse.presentation.ui.locations.list.LocationListFragment
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.ActivityBrowseBinding

class BrowseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrowseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initializeNavigation()
    }

    private fun initializeNavigation() {
        binding.navBarMain.setOnItemSelectedListener(navItemSelectedListener)
    }

    private val navItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_item_fragment_characters -> {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container_browse, CharacterListFragment())
                }
            }

            R.id.nav_item_fragment_episodes -> {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container_browse, EpisodeListFragment())
                }
            }

            R.id.nav_item_fragment_locations -> {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container_browse, LocationListFragment())
                }
            }

            else -> return@OnItemSelectedListener false
        }
        return@OnItemSelectedListener true
    }
}