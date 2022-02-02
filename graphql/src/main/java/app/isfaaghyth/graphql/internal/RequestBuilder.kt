package app.isfaaghyth.graphql.internal

import app.isfaaghyth.graphql.coroutines.await
import okhttp3.*

interface RequestBuilder {
    val url: String
    val body: RequestBody
    val headers: Headers
    val okHttpClient: OkHttpClient

    suspend fun request(): Response {
        val builder = Request.Builder()
            .url(url)
            .post(body)
            .headers(headers)
            .build()

        return okHttpClient
            .newCall(builder)
            .await()
    }
}