package com.tanu.spotifymusic.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tanu.spotifymusic.R
import com.tanu.spotifymusic.ui.auth.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var isSplashScreenVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { isSplashScreenVisible }
            setOnExitAnimationListener { splashScreenViewProvider ->
                val icon = splashScreenViewProvider.iconView

                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    isSplashScreenVisible = false
                    val scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0.4f, 0f)
                    val scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0.4f, 0f)

                    val animatorSet = AnimatorSet().apply {
                        playTogether(scaleX, scaleY)
                        duration = 300
                        doOnEnd {
                            splashScreenViewProvider.remove()
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                    animatorSet.start()
                }
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            isSplashScreenVisible = false
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
        }
    }
}