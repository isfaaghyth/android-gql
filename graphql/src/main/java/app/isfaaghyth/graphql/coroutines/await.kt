package app.isfaaghyth.graphql.coroutines

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Call.await(debug: Boolean = false): Response {
    val stackTrace = if (debug) {
        IOException().apply {
            /*
            * Remove unnecessary lines from stacktrace,
            * This doesn't remove await$default, but better than nothing
            * */
            stackTrace = stackTrace.copyOfRange(1, stackTrace.size)
        }
    } else {
        null
    }

    return suspendCancellableCoroutine {
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (it.isCancelled) return

                stackTrace?.initCause(e)
                it.resumeWithException(stackTrace?: e)
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume(response)
            }
        })

        it.invokeOnCancellation {
            try {
                cancel()
            } catch (ignored: Throwable) {}
        }
    }
}