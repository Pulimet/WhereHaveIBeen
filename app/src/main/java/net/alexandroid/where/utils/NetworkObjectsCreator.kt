package net.alexandroid.where.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkObjectsCreator {

    const val GOOGLE_SERACH_BASE_URL = "https://www.googleapis.com/"
    fun createOkHttpClient(logger: HttpLoggingInterceptor.Logger) = OkHttpClient.Builder()
        .connectTimeout(5L, TimeUnit.SECONDS)
        .readTimeout(10L, TimeUnit.SECONDS)
        .callTimeout(15L, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor(logger).apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(T::class.java)
    }
}