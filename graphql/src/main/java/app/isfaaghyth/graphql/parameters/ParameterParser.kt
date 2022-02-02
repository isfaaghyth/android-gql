package app.isfaaghyth.graphql.parameters

object ParameterParser {

    private const val pattern = "\\\$[A-Za-z|0-9]+(\\.[A-Za-z]+)*: (?:String|Int|Boolean)"
    private val regex = pattern.toRegex()

    /**
     * This parser will extract the parameter name and parameter data type from gql query
     *
     * @example:
     * query foo($bar: String) {
     *    foo_test(bar: $bar) {}
     * }
     *
     * the parser will return a map like this:
     * mapOf(
     *    "bar" to "java.lang.String"
     * )
     */
    fun parse(query: String): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            val map = this

            regex.findAll(query).forEach {
                val result = params(it.value)

                val paramName = result.first
                val paramType = result.second

                map[paramName] = paramType
            }
        }
    }

    private fun params(param: String): Pair<String, String> {
        /*
        * step 1:
        *
        * param = `$bar: String`
        * result = ["$bar", "String"]
        * */
        val result = param.split(":")

        /*
        * result.first() = `$bar`
        *
        * step 2:
        * transforming from `$bar` to `bar`
        *
        * variable = bar
        * */
        val variable = result.first().removePrefix("$").trim()

        /*
        * result.last() = `String`
        *
        * step 3:
        * append with `java.lang` for the data type
        *
        * dataType = `java.lang.String`
        * */
        val dataType = "java.lang.${result.last().trim().replaceInt()}"

        return Pair(variable, dataType)
    }

    /**
     * Because of gql used `Int` idiom,
     * we need to convert to `Integer` as supported variable data type on JVM
     */
    private fun String.replaceInt(): String {
        if (this == "Int") return "Integer"
        return this
    }

}