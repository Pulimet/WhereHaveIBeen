package net.alexandroid.where.utils

import android.content.Context
import androidx.annotation.StringRes

class GetResource(private val context: Context) {
    fun getString(@StringRes resId: Int) = context.getString(resId)
}