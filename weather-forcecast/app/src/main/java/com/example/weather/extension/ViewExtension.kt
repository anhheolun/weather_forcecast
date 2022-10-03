package com.example.weather.extension

import android.os.SystemClock
import android.view.View


fun View.setOnDebouncedClickListener(action: () -> Unit) {
    val actionDebounce = ActionDebounce(action)
    setOnClickListener {
        actionDebounce.notifyAction()
    }
}

private class ActionDebounce(private val action: () -> Unit) {
    private var lastActionTime = 0L

    fun notifyAction() {
        val now = SystemClock.elapsedRealtime()

        val millisecondsInterval = now - lastActionTime
        val actionAllowed = millisecondsInterval > DEBOUNCE_INTERVAL_MILLISECONDS
        lastActionTime = now

        if (actionAllowed) {
            action.invoke()
        }
    }

    companion object {
        const val DEBOUNCE_INTERVAL_MILLISECONDS = 600L
    }
}