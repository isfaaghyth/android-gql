package app.isfaaghyth.graphql.data

data class GqlResult<T>(
    val data: T?,
    val errors: List<GqlError?>,
)