package com.example.inventory.common

import android.os.Bundle
import androidx.lifecycle.Observer

class Event<T>(private val content: T) {
    var extra: Bundle? = null
    private var hasBeenHandled = false

    val contentIfNotHandled: T?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    fun peekContent(): T {
        return content
    }

    abstract class EventObserver<T>(private val content: EventUnhandledContent<T>?) :
        Observer<Event<out T>?> {
        override fun onChanged(value: Event<out T>?) {
            if (value != null) {
                val result = value.contentIfNotHandled
                if (result != null && content != null) {
                    content.onEventUnhandledContent(result)
                }
            }
        }
    }

    interface EventUnhandledContent<T> {
        fun onEventUnhandledContent(t: T)
    }
}