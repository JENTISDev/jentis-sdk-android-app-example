package com.jentis.jentisappexample

import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.jentis.jentisappexample.databinding.ActivityMainBinding
import com.jentis.sdk.jentissdk.JentisTrackService
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventsClicks()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    private fun eventsClicks() {
        binding.apply {

            btnSetNewUser.setOnClickListener {
                JentisTrackService.initialize(applicationContext)
                JentisTrackService.getInstance().initTracking()
                btnSetConsent.visibility = VISIBLE
            }

            btnSetConsent.setOnClickListener {
                JentisTrackService.getInstance().setConsent()
            }
        }
    }
}
