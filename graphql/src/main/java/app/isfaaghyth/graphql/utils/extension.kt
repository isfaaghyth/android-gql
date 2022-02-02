package app.isfaaghyth.graphql.utils

import app.isfaaghyth.graphql.data.GqlError

fun List<GqlError?>.message(): String {
    return mapNotNull { it?.message }.joinToString(separator = ", ")
}