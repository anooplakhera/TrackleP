package com.example.tracklep


import android.app.Application
import android.content.Context


class TrackleApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //        MultiDex.install(this);
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext

    }


    companion object {
        @get:Synchronized
        var instance: TrackleApp? = null
            private set
        var appContext: Context? = null
            private set
    }
}