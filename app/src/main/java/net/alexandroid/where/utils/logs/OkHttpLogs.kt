package net.alexandroid.where.utils.logs

import okhttp3.logging.HttpLoggingInterceptor

class OkHttpLogs : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        logI(message)
    }
}