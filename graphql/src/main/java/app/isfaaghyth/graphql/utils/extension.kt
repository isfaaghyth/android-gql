package app.isfaaghyth.graphql.utils

import app.isfaaghyth.graphql.data.GqlError

/**
 * Map the list of [GqlError] into String with joining multiple error message
 *
 * @param List of [GqlError]
 * @return [String]
 */
fun List<GqlError?>.message(): String {
    return mapNotNull { it?.message }.joinToString(separator = ", ")
}