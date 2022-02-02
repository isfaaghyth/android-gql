package app.isfaaghyth.graphql.parameters

import org.json.JSONObject

interface ParamBuilder {

    val request: JSONObject

    val parameters: JSONObject

    fun newBuilder(): Builder

    class Builder constructor(
        gqlQuery: String,
        private val request: JSONObject,
        private val parameters: JSONObject,
    ) {

        init {
            request.put(KEY_QUERY, gqlQuery)
        }

        fun putParam(param: Map<String, Any>) = apply {
            param.entries.forEach {
                val key = it.key
                val value = it.value

                if (!paramValidator(value)) error(
                    """
                        Seems you're put an expected parameter data type,
                        the gql only supported 3 types (String, Boolean, and Int
                    """.trimIndent()
                )

                parameters.put(key, param[key])
            }
        }

        override fun toString(): String {
            request.put(
                KEY_VARIABLES,
                parameters
            )

            return request.toString()
        }

    }

    companion object {
        private const val KEY_QUERY = "query"
        private const val KEY_VARIABLES = "variable"
    }

}