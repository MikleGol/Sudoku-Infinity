package liber.app.sudoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import liber.app.sudoku.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setFullScreen()

        val fadeIn: Animation = AlphaAnimation(0f, 1f).apply {
            interpolator = AccelerateInterpolator()
            duration = 2000
        }

        val liber_app_logo = findViewById<ImageView>(R.id.liber_app_logo)
        val relative_layout = findViewById<RelativeLayout>(R.id.relative_layout)

        liber_app_logo.startAnimation(fadeIn)
        relative_layout.startAnimation(fadeIn)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }, 3000)


    }

    private fun setFullScreen() {
        val decorView = window.decorView
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, insets ->
            val systemUiFlags = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_HIDE_NAVIGATION
            decorView.systemUiVisibility = systemUiFlags
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}