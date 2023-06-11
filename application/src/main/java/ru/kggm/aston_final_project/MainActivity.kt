package ru.kggm.aston_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kggm.feeature_main.presentation.ui.main.BlankFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_activity_main, BlankFragment())
                .commit()
        }
    }
}