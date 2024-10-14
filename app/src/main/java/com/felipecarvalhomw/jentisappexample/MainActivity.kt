package com.felipecarvalhomw.jentisappexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felipecarvalhomw.jentisappexample.databinding.ActivityMainBinding
import com.jentis.sdk.jentissdk.JentisTrackService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventsClicks()
    }

    private fun eventsClicks() {
        binding.apply {

            btnSetNewUser.setOnClickListener {
                JentisTrackService.initialize(applicationContext)
                JentisTrackService.getInstance().initTracking()
            }

            btnSetConsent.setOnClickListener {
                JentisTrackService.getInstance().setConsent()
            }
        }
    }
}
