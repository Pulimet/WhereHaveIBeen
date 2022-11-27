package net.alexandroid.where.utils

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

// Short way to collect StateFlow on coroutine when Activity Started.
// Useful when you need to collect one StateFlow, but could be used for many.
fun <T> Flow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect {
                function.invoke(it)
            }
        }
    }
}

@Suppress("unused")
fun ViewModel.emitSharedFlow(mutableSharedFlow: MutableSharedFlow<Unit>) {
    viewModelScope.launch { mutableSharedFlow.emit(Unit) }
}

// Set on click listener
@Suppress("unused")
fun View.OnClickListener.setOnClickListeners(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}

// Toasts
@Suppress("unused")
fun Fragment.showToast(@StringRes resource: Int) {
    showToast(getString(resource))
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Location

fun String.toDegreeFormat(): Double {
    val dotIndex = if (this[0] == '-') {
        if (length == 10) 3 else 2
    } else {
        if (length == 9) 2 else 1
    }

    return (this.substring(0, dotIndex) + "." + this.substring(dotIndex)).toDouble()
}