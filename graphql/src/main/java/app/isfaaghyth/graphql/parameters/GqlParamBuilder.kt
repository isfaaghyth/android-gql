package app.isfaaghyth.graphql.parameters

import org.json.JSONObject

class GqlParamBuilder constructor(
    private val gqlQuery: String
) : ParamBuilder {

    override val request: JSONObject
        get() = JSONObject()

    override val parameters: JSONObject
        get() = JSONObject()

    override fun newBuilder(): ParamBuilder.Builder {
        return ParamBuilder.Builder(
            gqlQuery,
            request,
            parameters
        )
    }

}