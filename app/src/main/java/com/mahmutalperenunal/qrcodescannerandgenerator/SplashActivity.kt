package com.mahmutalperenunal.qrcodescannerandgenerator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mahmutalperenunal.qrcodescannerandgenerator.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set system bar color
        window.navigationBarColor = (ContextCompat.getColor(this, R.color.splash_screen_color))
        window.statusBarColor = (ContextCompat.getColor(this, R.color.splash_screen_color))

        //open login activity with timer
        binding.apply {
            splashLogoImageView.alpha = 0f
            splashLogoImageView.animate().setDuration(1500).alpha(1f).withEndAction{
                val intent = Intent(this@SplashActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        }
    }
}