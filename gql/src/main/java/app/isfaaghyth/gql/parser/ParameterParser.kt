package app.isfaaghyth.gql.parser

object ParameterParser {

    // currently. graphql only supported for 3 data types.
    private const val pattern = "\\\$[A-Za-z|0-9]+(\\.[A-Za-z]+)*: (?:String|Int|Boolean)"
    private val regex = Regex(pattern)

    fun parse(query: String): Map<String, Any> {
        val temp = mutableMapOf<String, Any>()
        regex.findAll(query).forEach {
            val result = separateParams(it.value)
            temp[result.first] = result.second
        }
        return temp
    }

    // TODO: finding the best regex pattern to separate between variable name and type
    private fun separateParams(param: String): Pair<String, String> {
        val result = param.split(":")
        val variable = result[0].drop(1).trim() // .drop(1) to remove $ in first character
        val dataType = "java.lang.${result[1].trim().replaceInt()}" // convert to java.lang.*
        return Pair(variable, dataType)
    }

    private fun String.replaceInt(): String {
        if (this == "Int") return "Integer"
        return this
    }

}