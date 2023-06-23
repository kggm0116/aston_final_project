package ru.kggm.application

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.kggm.application.R
import ru.kggm.feature_browse.presentation.ui.BrowseActivity
//import ru.kggm.feature_browse .R as mainR
import ru.kggm.application.R as appR
import ru.kggm.core.R as coreR

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(appR.layout.activity_splash)
        window.decorView.rootView.postDelayed({
            startActivity(Intent(this, BrowseActivity::class.java))
            finish()
        }, 2000)
    }
}