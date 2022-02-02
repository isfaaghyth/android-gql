package app.isfaaghyth.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.isfaaghyth.graphql.Gql
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gqlQuery = """
            query launchesPast(${'$'}limit: Int) {
              launchesPast(limit: ${'$'}limit) {
                mission_name
                launch_date_local
                launch_site {
                  site_name_long
                }
              }
            }
        """.trimIndent()

        launch(Dispatchers.IO) {
            Gql.query(gqlQuery)
                .parameters(mapOf(
                    "limit" to 10
                ))
                .get(
                    onSuccess = {
                        withContext(Dispatchers.Main) {
                            println(it)
                        }
                    },
                    onError = {
                        withContext(Dispatchers.Main) {
                            println(it.message)
                        }
                    }
                )
        }
    }

}