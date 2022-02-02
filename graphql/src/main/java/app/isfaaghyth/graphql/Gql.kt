@file:Suppress("BlockingMethodInNonBlockingContext")

package app.isfaaghyth.graphql

import app.isfaaghyth.graphql.data.GqlResult
import app.isfaaghyth.graphql.internal.GqlRequestBuilder
import app.isfaaghyth.graphql.internal.parameters.Network
import app.isfaaghyth.graphql.internal.parameters.Request
import app.isfaaghyth.graphql.utils.MessageErrorException
import app.isfaaghyth.graphql.utils.message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Response

object Gql {

    // network
    private var url: String = ""
    private val headers = mutableMapOf<String, String>()
    private var okHttpClient: OkHttpClient? = null

    // request
    private var query: String = ""
    private val parameters = mutableMapOf<String, Map<String, Any>>()

    fun init(url: String, okHttpClient: OkHttpClient? = null) {
        this.url = url
        this.okHttpClient = okHttpClient
    }

    fun headers(headers: Map<String, String>) = apply {
        this.headers.clear()
        this.headers.putAll(headers)
    }

    fun query(value: String) = apply {
        query = value
    }

    fun parameters(params: Map<String, Any>) = apply {
        require(query.isNotEmpty()) { "query not found" }

        parameters.clear()
        parameters.putAll(mapOf(
            query to params
        ))
    }

    suspend inline fun <reified T> get(
        onSuccess: (T) -> Unit,
        onError: (MessageErrorException) -> Unit = {}
    ) {
        try {
            val result = get<T>()

            if (result.data != null) {
                onSuccess(result.data)
            } else {
                val errorMessage = result.errors.message()
                onError(MessageErrorException(errorMessage))
            }
        } catch (e: Throwable) {
            onError(MessageErrorException(e.stackTraceToString()))
        }
    }

    suspend inline fun <reified T> get(): GqlResult<T> {
        val response = request().body?.string()
        val type = object : TypeToken<GqlResult<T>>() {}.type
        return Gson().fromJson(response, type)
    }

    @PublishedApi
    internal suspend fun request(): Response {
        require(url.isNotEmpty()) { "url not found" }
        require(query.isNotEmpty()) { "query not found" }

        return GqlRequestBuilder(
            Network(url, headers, okHttpClient),
            Request(query, parameters[query])
        ).request()
    }

}