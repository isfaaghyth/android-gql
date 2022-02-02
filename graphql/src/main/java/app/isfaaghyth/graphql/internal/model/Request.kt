package app.isfaaghyth.graphql.internal.model

data class Request(
    val query: String,
    val parameters: Map<String, Any>?,
)