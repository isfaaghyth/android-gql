package app.isfaaghyth.graphql.parameters

import org.json.JSONObject

class GqlParamBuilder constructor(
    private val gqlQuery: String
) : ParamBuilder {

    override fun newBuilder() = ParamBuilder.Builder(
        gqlQuery = gqlQuery,
        request = JSONObject(),
        parameters = JSONObject()
    )

}