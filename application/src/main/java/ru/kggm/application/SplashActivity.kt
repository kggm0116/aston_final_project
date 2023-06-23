package ru.kggm.application

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.kggm.feature_browse.presentation.ui.BrowseActivity
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import ru.kggm.application.R as appR

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    companion object {
        // Who needs view models?..
        private var wasShown = AtomicBoolean(false)
        private var expired = AtomicBoolean(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (expired.get()) {
            openMainActivity()
        }

        setContentView(appR.layout.activity_splash)

        if (!wasShown.getAndSet(true)) {
            thread {
                Thread.sleep(1000L)
                expired.set(true)
                openMainActivity()
            }
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, BrowseActivity::class.java))
        finish()
    }
}