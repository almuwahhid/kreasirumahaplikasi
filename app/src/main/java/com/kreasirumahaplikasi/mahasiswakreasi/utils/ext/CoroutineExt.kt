package com.kreasirumahaplikasi.mahasiswakreasi.utils.ext

import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun coroutineLaunch(
    context: CoroutineContext = Dispatchers.Default,
    runner: suspend CoroutineScope.() -> Unit
): Job =
    CoroutineScope(context).launch { runner.invoke((this)) }

fun <T> coroutineAsync(
    context: CoroutineContext = Dispatchers.Default,
    runner: suspend CoroutineScope.() -> T
): Deferred<T> =
    CoroutineScope(context).async { runner.invoke((this)) }

suspend fun <T> CoroutineScope.await(block: () -> Deferred<T>): T = block().await()

suspend inline fun <reified T> CoroutineScope.awaitAsync(
    context: CoroutineContext = Dispatchers.Default,
    crossinline block: () -> T
): T =
    withContext(context) { block() }

suspend fun <T> asyncTask(function: () -> T): T {
    return withContext(Dispatchers.Default) { function() }
}

fun <T> lazyPromiseLocal(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        CoroutineScope(Dispatchers.Default)
            .async(start = CoroutineStart.LAZY) { block.invoke(this) }
    }
}

fun <T> lazyPromiseGlobal(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

suspend inline fun File?.await(): File = suspendCoroutine { cont ->
    if (this != null) cont.resume(this)
    else cont.resumeWithException(RuntimeException("Unknown file"))
}