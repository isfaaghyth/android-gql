package app.isfaaghyth.graphql.internal

import app.isfaaghyth.graphql.internal.parameters.Network
import app.isfaaghyth.graphql.internal.parameters.Request
import org.junit.Test

class GqlRequestBuilderTest {

    @Test
    fun `invalid parameter`() {
        // Given
        val builder = GqlRequestBuilder(
            Network(
                "",
                mapOf(),
                null
            ),
            Request(
                "",
                mapOf()
            )
        )
    }

}