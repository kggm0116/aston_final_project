package ru.kggm.aston_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import ru.kggm.feeature_characters.presentation.ui.CharactersFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: CharacterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
//        (application as Application).component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_activity_main, CharactersFragment())
                .commit()
        }

//        DaggerApplicationComponent.builder()
//            .build()
//            .inject(this)
    }
}