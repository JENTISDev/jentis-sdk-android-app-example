package com.jentis.jentisappexample

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

        val container = "ckion-demo"
        val environment = "live"
        val version = "3"
        val debugCode = "a675b5f1-48d2-43bf-b314-ba4830cda52d"
        val trackDomain = "https://qc3ipx.ckion-dev.jtm-demo.com"

        JentisTrackService.getInstance().initTracking(
            application = this,
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode
        )
    }
}