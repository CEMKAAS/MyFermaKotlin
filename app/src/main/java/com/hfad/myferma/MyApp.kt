package com.hfad.myferma

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

class MyApp : Application() {
   override fun onCreate() {
        super.onCreate()
        val config =
            AppMetricaConfig.newConfigBuilder("1b0719ae-9f60-4ac0-98c9-5af8cea092b4").build()
        AppMetrica.activate(this, config)
    }
}