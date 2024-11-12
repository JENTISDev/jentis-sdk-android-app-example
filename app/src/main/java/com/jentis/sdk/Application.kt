package com.jentis.sdk

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jentis.sdk.jentissdk.JentisTrackService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor.Builder(applicationContext).build())
            .build()

        JentisTrackService.initialize(applicationContext, okHttpClient)

        val sharedPreferences = getSharedPreferences("config", MODE_PRIVATE)

        val container = sharedPreferences.getString("container", "ckion-demo") ?: "ckion-demo"
        val environment = sharedPreferences.getString("environment", "live") ?: "live"
        val version = sharedPreferences.getString("version", "3") ?: "3"
        val debugCode = sharedPreferences.getString("debugCode", "a675b5f1-48d2-43bf-b314-ba4830cda52d") ?: "a675b5f1-48d2-43bf-b314-ba4830cda52d"
        val trackDomain = sharedPreferences.getString("trackDomain", "https://qc3ipx.ckion-dev.jtm-demo.com") ?: "https://qc3ipx.ckion-dev.jtm-demo.com"
        val authToken = "token-test"

        JentisTrackService.getInstance().initTracking(
            application = this,
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode,
            authToken = authToken
        )
    }
}