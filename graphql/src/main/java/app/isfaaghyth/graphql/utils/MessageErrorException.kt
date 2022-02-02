package app.isfaaghyth.graphql.utils

import java.io.IOException

/**
 * We need to unified the error exception for every single error result
 *
 * @exception IOException
 */
class MessageErrorException(
    override val message: String?
) : IOException()