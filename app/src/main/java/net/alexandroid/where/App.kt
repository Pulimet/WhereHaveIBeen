package net.alexandroid.where

import android.app.Application
import net.alexandroid.where.koin.Koin
import net.alexandroid.where.utils.logs.logD

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        logD("=== Application onCreate() ===")
        Koin.setup(applicationContext)
    }
}