package app.isfaaghyth.graphql.internal

import app.isfaaghyth.graphql.internal.model.Network
import app.isfaaghyth.graphql.internal.model.Request
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

    /**
     * Validator of graphql parameters
     *
     * @param params
     */
    private fun isQueryParamAcceptable(params: Map<String, Any>) {
        // first, we need to parse and extract the parameter of gql from query
        val gqlParamDataType = mutableMapOf<String, Any>()
        val parameters = parameterParser(request.query)

        // second, add all parameters are indicated on gql query
        gqlParamDataType.clear()
        gqlParamDataType.putAll(parameters)

        /*
        * we must validate the number of parameters received from the user,
        * the amount of parameter must match with the indicated parameters.
        * */
        if (gqlParamDataType.size != params.size) {
            throw ArrayIndexOutOfBoundsException("the amount of parameter must match with the indicated parameters.")
        }

        /*
        * after validating the amount of parameters,
        * we need to ensure the data type also is match.
        * */
        val invalidParams = mutableListOf<String>()

        gqlParamDataType.keys.forEach {
            if (Class.forName(gqlParamDataType[it].toString()) != params[it]?.javaClass) {
                invalidParams.add(it)
            }
        }

        if (invalidParams.isNotEmpty()) {
            throw IllegalArgumentException("your data type of $invalidParams is not match with parameter on gql query.")
        }
    }

    /**
     * Create request body from gql query parameter
     *
     * @return [RequestBody]
     */
    private fun buildRequestBody(): RequestBody {
        // get parameters from user's input
        val queryParameters = request.parameters?: mapOf()

        // validation between the parameters input and gql query param
        isQueryParamAcceptable(queryParameters)

        // create request body based on parameters
        return GqlParamBuilder(request.query)
            .newBuilder()
            .putParam(queryParameters)
            .toString()
            .toRequestBody(mediaType)
    }

    /**
     * Set custom headers if any
     *
     * @return [Headers]
     */
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

    /**
     * Create the default okhttp client request if there's no custom builder
     *
     * @return [OkHttpClient]
     */
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