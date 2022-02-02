@file:Suppress("BlockingMethodInNonBlockingContext")

package app.isfaaghyth.graphql

import app.isfaaghyth.graphql.data.GqlResult
import app.isfaaghyth.graphql.internal.GqlRequestBuilder
import app.isfaaghyth.graphql.internal.model.Network
import app.isfaaghyth.graphql.internal.model.Request
import app.isfaaghyth.graphql.utils.MessageErrorException
import app.isfaaghyth.graphql.utils.message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Response

object Gql {

    // --- network ---
    private var url: String = ""
    private val headers = mutableMapOf<String, String>()
    private var okHttpClient: OkHttpClient? = null

    // --- request ---
    private var query: String = ""
    private val parameters = mutableMapOf<String, Map<String, Any>>()

    /**
     * Initializes Gql with url and custom okHttpClient builder (optional)
     *
     * @param url
     * @param okHttpClient
     */
    fun init(url: String, okHttpClient: OkHttpClient? = null) {
        this.url = url
        this.okHttpClient = okHttpClient
    }

    /**
     * Add a custom headers for network request
     *
     * @param headers
     */
    fun headers(headers: Map<String, String>) = apply {
        this.headers.clear()
        this.headers.putAll(headers)
    }

    /**
     * set gql query (supported for query, mutation, nested-query, or multiple query).
     *
     * @param q
     */
    fun query(q: String) = apply {
        query = q
    }

    /**
     * setup graphql query parameters. The parameters will matching the data type and param name
     * automatically based on graphql query
     *
     * @param params
     */
    fun parameters(params: Map<String, Any>) = apply {
        require(query.isNotEmpty()) { "query not found" }

        parameters.clear()
        parameters.putAll(mapOf(
            query to params
        ))
    }

    /**
     * graphql request handler with custom data model and using callback
     *
     * @param onSuccess
     * @param onError
     */
    @JvmName("getAsObject")
    suspend inline fun <reified T> get(
        onSuccess: (T) -> Unit,
        onError: (MessageErrorException) -> Unit = {}
    ) {
        try {
            val result = getAsModel<T>()

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

    /**
     * graphql request handler with a plain string response using callback
     *
     * @param onSuccess
     * @param onError
     */
    @JvmName("getAsString")
    suspend inline fun get(
        onSuccess: (String) -> Unit,
        onError: (MessageErrorException) -> Unit = {}
    ) {
        try {
            val result = getAsString()?: ""
            onSuccess(result)
        } catch (e: Throwable) {
            onError(MessageErrorException(e.stackTraceToString()))
        }
    }

    /**
     * graphql request handler return a data model without callback
     *
     * @return [GqlResult]
     */
    suspend inline fun <reified T> getAsModel(): GqlResult<T> {
        val type = object : TypeToken<GqlResult<T>>() {}.type
        return Gson().fromJson(getAsString(), type)
    }

    /**
     * graphql request handler return a plain string without callback
     *
     * @return [String]
     */
    suspend fun getAsString(): String? {
        return request().body?.string()
    }

    /**
     * execute graphql request using [GqlRequestBuilder].
     * The executors will mapping the url, headers, and httpClient as Network,
     * and mapping the query and parameters as Data Request
     *
     * @return [Response]
     */
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