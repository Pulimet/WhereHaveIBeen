package net.alexandroid.where.koin

import android.content.Context
import android.location.Geocoder
import androidx.room.Room
import com.google.gson.Gson
import net.alexandroid.where.db.LocationsDatabase
import net.alexandroid.where.repo.LocationsRepo
import net.alexandroid.where.ui.map.MapViewModel
import net.alexandroid.where.ui.tutorial.TutorialViewModel
import net.alexandroid.where.ui.upload.UploadViewModel
import net.alexandroid.where.utils.LocationUtils
import net.alexandroid.where.utils.NetworkObjectsCreator
import net.alexandroid.where.utils.logs.KoinLogs
import net.alexandroid.where.utils.logs.OkHttpLogs
import net.alexandroid.where.utils.logs.logI
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.util.Locale
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
        single { Gson() }
        single { Geocoder(androidContext(), Locale.getDefault()) }
        singleOf(::LocationUtils)

        // ViewModels
        singleOf(::UploadViewModel)
        singleOf(::MapViewModel)
        singleOf(::TutorialViewModel)

        // Repos
        singleOf(::LocationsRepo)

        // Room
        single {
            Room.databaseBuilder(
                androidContext(), LocationsDatabase::class.java, "locations_database"
            ).build()
        }
        single { get<LocationsDatabase>().locationDao() }

        // Network
        single<HttpLoggingInterceptor.Logger> { OkHttpLogs() }
        single { NetworkObjectsCreator.createOkHttpClient(get()) }
        //single { createWebService<TmdbApiService>(get(), NetworkConstants.TMDB_URL) }
    }
}