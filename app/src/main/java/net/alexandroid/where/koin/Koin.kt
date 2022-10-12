package net.alexandroid.where.koin

import android.content.Context
import net.alexandroid.where.utils.NetworkObjectsCreator
import net.alexandroid.where.utils.logs.KoinLogs
import net.alexandroid.where.utils.logs.OkHttpLogs
import net.alexandroid.where.utils.logs.logI
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.system.measureTimeMillis

object Koin {
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

        // Room
        /*single {
            Room.databaseBuilder(
                androidContext(), XxxDatabase::class.java, "xxx_database"
            ).build()
        }
        single { get<XxxDatabase>().movieDao() }*/

        // Network
        single<HttpLoggingInterceptor.Logger> { OkHttpLogs() }
        single { NetworkObjectsCreator.createOkHttpClient(get()) }
        //single { createWebService<TmdbApiService>(get(), NetworkConstants.TMDB_URL) }
    }
}