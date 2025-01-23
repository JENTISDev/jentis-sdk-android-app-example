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
        val container = prefsManager.getString("container", "mipion-demo")
        val version = prefsManager.getString("version", "3")
        val debugCode = prefsManager.getString("debugCode", "44c2acd3-434d-4234-983b-48e91551eb5a")
        val sessionTimeout = prefsManager.getString("sessionTimeout", "1800")
        val environment = prefsManager.getString("environment", "live")
        val authToken = "22fef7a3b00466743fee2ab8cd8afb01"
        val enableOfflineTracking = true
        val sessionTimeoutInSeconds = 1800L

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
            authToken = authToken,
            sessionTimeoutParam = sessionTimeout.toIntOrNull(),
            protocol = protocol,
            enableOfflineTracking = enableOfflineTracking,
            offlineTimeout = sessionTimeoutInSeconds
        )
    }
}
