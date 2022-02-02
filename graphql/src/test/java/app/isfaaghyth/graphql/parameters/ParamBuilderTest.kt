package app.isfaaghyth.graphql.parameters

import org.junit.Assert.assertEquals
import org.junit.Test

class ParamBuilderTest {

    private val gqlParameters = mapOf(
        "name" to "Isfa",
        "age" to 24
    )

    @Test
    fun `build gql parameter`() {
        // When
        val query = GqlParamBuilder(sampleQuery)
            .newBuilder()
            .putParam(gqlParameters)
            .toString()

        // Then
        assertEquals(expectedValue(), query)
    }

    @Test(expected = IllegalStateException::class)
    fun `build gql parameter with invalid data type`() {
        GqlParamBuilder(sampleQuery)
            .newBuilder()
            .putParam(mapOf(
                "weight" to 3.14f
            ))
            .toString()
    }

    private fun expectedValue(): String {
        return """
            {"query":"$sampleQuery","variable":{"name":"${gqlParameters["name"]}","age":${gqlParameters["age"]}}}
        """.trimIndent()
    }

    companion object {
        val sampleQuery = """
            query user_info(){}
        """.trimIndent()
    }

}