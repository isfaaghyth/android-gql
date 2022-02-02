package app.isfaaghyth.graphql.utils

import java.io.IOException

class MessageErrorException(
    override val message: String?
) : IOException()