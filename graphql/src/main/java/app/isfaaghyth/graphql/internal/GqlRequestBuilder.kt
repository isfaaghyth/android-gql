package app.isfaaghyth.graphql.internal

import app.isfaaghyth.graphql.internal.parameters.Network
import app.isfaaghyth.graphql.internal.parameters.Request
import app.isfaaghyth.graphql.parameters.GqlParamBuilder
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import app.isfaaghyth.graphql.parameters.ParameterParser.parse as parameterParser

class GqlRequestBuilder constructor(
    private val network: Network,
    private val request: Request
) : RequestBuilder {

    override val url: String
        get() = network.url

    override val body: RequestBody
        get() = buildRequestBody()

    override val headers: Headers
        get() = buildHeaders()

    override val okHttpClient: OkHttpClient
        get() = network.okHttpClient?: defaultOkHttpClient()

    private val gqlParams = mutableMapOf<String, Any>()

    private fun buildRequestBody(): RequestBody {
        val queryParameters = request.parameters?: mapOf()

        getOrCreateGqlParams(queryParameters)

        return GqlParamBuilder(request.query)
            .newBuilder()
            .putParam(queryParameters)
            .toString()
            .toRequestBody(mediaType)
    }

    private fun buildHeaders(): Headers {
        return Headers.Builder().apply {
            if (network.headers.isNotEmpty()) {
                network.headers.forEach {
                    val name = it.key
                    val value = it.value

                    add(name, value)
                }
            }
        }.build()
    }

    private fun getOrCreateGqlParams(params: Map<String, Any>) {
        // first, add all parameters are indicated on gql query
        gqlParams.clear()
        gqlParams.putAll(parameterParser(request.query))

        /*
        * we must validate the number of parameters received from the user,
        * the amount of parameter must match with the indicated parameters.
        * */
        if (gqlParams.size != params.size)
            throw ArrayIndexOutOfBoundsException("the amount of parameter must match with the indicated parameters.")

        /*
        * after validating the amount of parameters,
        * we need to ensure the data type also is match.
        * */
        gqlParams.keys.forEach {
            if (Class.forName(gqlParams[it] as String) != params[it]?.javaClass) {
                throw IllegalArgumentException("your data type of $it is not match with parameter on gql query.")
            }
        }
    }

    private fun defaultOkHttpClient() =
        OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()

    companion object {
        private val mediaType = "application/json; charset=utf-8".toMediaType()
        private const val REQUEST_TIMEOUT = 60L
    }

}