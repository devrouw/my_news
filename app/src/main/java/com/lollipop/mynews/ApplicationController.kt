package com.lollipop.mynews

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ApplicationController : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}