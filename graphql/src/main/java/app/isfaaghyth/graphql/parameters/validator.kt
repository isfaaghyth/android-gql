package app.isfaaghyth.graphql.parameters

/**
 * Because of gql only supported 3 data types,
 * we need this validator to ensure the parameters
 * should be as expected.
 */
fun paramValidator(arg: Any): Boolean {
    return (arg is String || arg is Boolean || arg is Int)
}