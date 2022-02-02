package app.isfaaghyth.graphql.internal.parameters

import okhttp3.OkHttpClient

data class Network(
    val url: String,
    val headers: Map<String, String>,
    val okHttpClient: OkHttpClient?
)