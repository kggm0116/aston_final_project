package ru.kggm.aston_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationBarView
import ru.kggm.aston_final_project.databinding.ActivityMainBinding
import ru.kggm.feature_browse.presentation.ui.characters.CharactersFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initializeNavigation()
    }

    private fun initializeNavigation() {
        binding.navBarMain.setOnItemSelectedListener(navItemSelectedListener)
    }

    private val navItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_item_fragment_characters -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_activity_main, CharactersFragment())
                    .commit()
                true
            }

            R.id.nav_item_fragment_episodes -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_activity_main, CharactersFragment())
                    .commit()
                true
            }

            R.id.nav_item_fragment_locations -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_activity_main, CharactersFragment())
                    .commit()
                true
            }

            else -> false
        }
    }
}