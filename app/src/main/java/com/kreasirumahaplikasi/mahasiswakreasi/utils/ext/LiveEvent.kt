package com.kreasirumahaplikasi.mahasiswakreasi.utils.ext

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.*

class LiveEvent<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}

fun <T> LiveData<T>.toSingleEvent(): LiveEvent<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}

/**
 * Handle only non-nullable values observed to prevent NPE with SingleEvent
 */
@Deprecated(
    "Use observes() instead to handle non-nullable LiveData.\n" +
            "Also use toSingleEvent inside your repository class to prevent another observer assigned to that variable",
    ReplaceWith("observeSingle", "observes")
)
inline fun <T> LiveData<T>.observeSingle(
    owner: LifecycleOwner,
    crossinline handleContent: (T) -> Unit
) {
    toSingleEvent().observe(owner, Observer {
        it?.let(handleContent)
    })
}

inline fun <T> LiveData<T>.observes(
    owner: LifecycleOwner,
    crossinline handleContent: (T) -> Unit
) {
    observe(owner, Observer {
        it?.let(handleContent)
    })
}

@MainThread
fun <T> MutableLiveData<T>.call() {
    value = null
}