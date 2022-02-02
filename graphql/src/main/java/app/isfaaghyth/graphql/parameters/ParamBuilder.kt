package app.isfaaghyth.graphql.parameters

import org.json.JSONObject

interface ParamBuilder {

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

                if (!isParamValid(value)) error(
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

        /**
         * Because of gql only supported 3 data types,
         * we need this validator to ensure the parameters
         * should be as expected.
         */
        private fun isParamValid(arg: Any): Boolean {
            return (arg is String || arg is Int || arg is Boolean)
        }

    }

    companion object {
        private const val KEY_QUERY = "query"
        private const val KEY_VARIABLES = "variable"
    }

}