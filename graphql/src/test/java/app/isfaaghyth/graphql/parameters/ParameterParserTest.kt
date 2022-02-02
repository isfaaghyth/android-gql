package app.isfaaghyth.graphql.parameters

import org.junit.Assert.assertEquals
import org.junit.Test

class ParameterParserTest {

    @Test
    fun `parse simple query`() {
        // Given
        val sampleQuery = """
            query user_info(${'$'}name: String, ${'$'}age: Int){
                userinfo(name: ${'$'}name, age: ${'$'}age){
                    data
                    status
                }
            }
        """.trimIndent()

        // When
        val result = ParameterParser.parse(sampleQuery)

        // Then
        val expectedValue = mapOf(
            "name" to "java.lang.String",
            "age" to "java.lang.Integer"
        )

        assertEquals(expectedValue, result)
    }

    @Test
    fun `parse simple mutation`() {
        // Given
        val sampleQuery = """
            mutation user_validation(${'$'}token: String){
                generateToken(name: ${'$'}token){
                    token
                }
            }
        """.trimIndent()

        // When
        val result = ParameterParser.parse(sampleQuery)

        // Then
        val expectedValue = mapOf(
            "token" to "java.lang.String"
        )

        assertEquals(expectedValue, result)
    }

    @Test
    fun `parse multiple query`() {
        // Given
        val sampleQuery = """
            query user_info(${'$'}name: String, ${'$'}age: Int){
                userinfo(name: ${'$'}name, age: ${'$'}age){
                    data
                    status
                }
            }
            
            query user_info_v2(${'$'}email: String, ${'$'}type: Int){
                userinfo(email: ${'$'}email, type: ${'$'}type){
                    data
                    status
                }
            }
        """.trimIndent()

        // When
        val result = ParameterParser.parse(sampleQuery)

        // Then
        val expectedValue = mapOf(
            "name" to "java.lang.String",
            "email" to "java.lang.String",
            "age" to "java.lang.Integer",
            "type" to "java.lang.Integer"
        )

        assertEquals(expectedValue, result)
    }

    @Test
    fun `parse nested query`() {
        // Given
        val sampleQuery = """
            query user_info(${'$'}name: String, ${'$'}age: Int){
                userinfo(name: ${'$'}name, age: ${'$'}age){
                    data
                    status
                }
                
                query user_detail(${'$'}email: String, ${'$'}type: Int){
                    userinfo(email: ${'$'}email, type: ${'$'}type){
                        data
                        status
                    }
                }
            }
        """.trimIndent()

        // When
        val result = ParameterParser.parse(sampleQuery)

        // Then
        val expectedValue = mapOf(
            "name" to "java.lang.String",
            "email" to "java.lang.String",
            "age" to "java.lang.Integer",
            "type" to "java.lang.Integer"
        )

        assertEquals(expectedValue, result)
    }

}