package net.alexandroid.koin.di

import android.content.Context
import net.alexandroid.where.utils.logs.KoinLogs
import net.alexandroid.where.utils.logs.logI
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.system.measureTimeMillis

object Di {
    fun setup(applicationContext: Context) {
        val timeInMillis = measureTimeMillis {
            startKoin {
                logger(KoinLogs())
                androidContext(applicationContext)
                modules(appModule)
            }
        }
        logI("=== DI is ready (timeInMillis: $timeInMillis)===")
    }

    private val appModule = module {

    }
}