package com.jentis.jentisappexample

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.jentis.jentisappexample.databinding.ActivityMainBinding
import com.jentis.sdk.jentissdk.JentisTrackService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var vendorConsents = mutableMapOf(
        "googleanalytics" to "allow",
        "facebook" to "deny",
        "awin" to "ncm"
    )
    private var vendorConsentsChanged = mutableMapOf<String, String>()

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

        setupVendorSpinners()
    }

    private fun setupVendorSpinners() {
        val vendorOptions = resources.getStringArray(R.array.vendor_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vendorOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        setupSpinner(binding.spinnerGoogleAnalytics, "googleanalytics", adapter)
        setupSpinner(binding.spinnerFacebook, "facebook", adapter)
        setupSpinner(binding.spinnerAwin, "awin", adapter)
    }

    private fun setupSpinner(spinner: Spinner, vendor: String, adapter: ArrayAdapter<String>) {
        spinner.adapter = adapter
        spinner.setSelection(adapter.getPosition(vendorConsents[vendor]))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedConsent = parent.getItemAtPosition(position).toString()
                vendorConsents[vendor] = selectedConsent
                vendorConsentsChanged[vendor] = selectedConsent
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun eventsClicks() {
        binding.apply {
            btnSetConsent.setOnClickListener {
                JentisTrackService.getInstance().setConsent()
            }

            btnSendDataSubmission.setOnClickListener {
                JentisTrackService.getInstance().sendDataSubmission()
                btnSetConsent.visibility = View.VISIBLE
            }

            btnAddToPushQueue.setOnClickListener {
                prepareItemPushQueue()
                JentisTrackService.getInstance().addToPush()
            }

            btnSubmitPushData.setOnClickListener {
                submitPushData()
                JentisTrackService.getInstance().submitPushing()
            }

            btnSendVendors.setOnClickListener {
                vendorBox.visibility =
                    if (vendorBox.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                vendorConsentsChanged.clear()
            }

            btnSetVendors.setOnClickListener {
                JentisTrackService.getInstance().setVendors(vendorConsents, vendorConsentsChanged)
                vendorBox.visibility = View.GONE
            }
        }
    }

    private fun prepareItemPushQueue() {
    }

    private fun submitPushData() {
    }
}
