package com.jentis.jentisappexample

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jentis.jentisappexample.fragments.localdata.SharedPreferencesManager
import com.jentis.sdk.jentissdk.JentisTrackService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefsManager = SharedPreferencesManager(this)

        val protocol = prefsManager.getString("protocol", "https://")
        val trackDomain = prefsManager.getString("trackDomain", "nd7cud.mobiweb.jtm-demo.com")
        val container = prefsManager.getString("container", "mobiweb-demoshop")
        val version = prefsManager.getString("version", "1")
        val debugCode = prefsManager.getString("debugCode", "44c2acd3-43d4-4234-983b-48e91")
        val sessionTimeout = prefsManager.getString("sessionTimeout", "180")
        val environment = prefsManager.getString("environment", "live")

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor.Builder(applicationContext).build())
            .build()

        JentisTrackService.initialize(applicationContext, okHttpClient)

        JentisTrackService.getInstance().initTracking(
            application = this,
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode,
            authToken = null,
            sessionTimeoutParam = sessionTimeout.toIntOrNull(),
            protocol = protocol
        )
    }
}
