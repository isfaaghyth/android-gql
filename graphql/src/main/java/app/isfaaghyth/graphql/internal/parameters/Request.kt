package app.isfaaghyth.graphql.internal.parameters

data class Request(
    val query: String,
    val parameters: Map<String, Any>?,
)