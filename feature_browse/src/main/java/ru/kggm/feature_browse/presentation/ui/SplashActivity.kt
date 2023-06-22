package ru.kggm.feature_browse.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ru.kggm.feature_main.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.decorView.rootView.postDelayed({
            startActivity(Intent(this, BrowseActivity::class.java))
            finish()
        }, 2000)
    }
}