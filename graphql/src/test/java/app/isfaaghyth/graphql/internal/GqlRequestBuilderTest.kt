package app.isfaaghyth.graphql.internal

import app.isfaaghyth.graphql.internal.model.Network
import app.isfaaghyth.graphql.internal.model.Request
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