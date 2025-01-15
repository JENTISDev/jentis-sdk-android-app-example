package com.jentis.jentisappexample

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jentis.jentisappexample.fragments.localdata.SharedPreferencesManager
import com.jentis.sdk.jentissdk.JentisTrackService
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

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

        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, null)

        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(
                sslContext.socketFactory,
                trustManagerFactory.trustManagers[0] as X509TrustManager
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor.Builder(applicationContext).build())
            .build()


        JentisTrackService.initialize(this, okHttpClient)

        JentisTrackService.getInstance().initTracking(
            application = this,
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode,
            authToken = null,
            sessionTimeoutParam = sessionTimeout.toIntOrNull(),
            protocol = protocol,
            enableOfflineTracking = true,
            offlineTimeout = 60
        )
    }
}
