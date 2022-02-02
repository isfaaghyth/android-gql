package app.isfaaghyth.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.isfaaghyth.graphql.Gql
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Gql.init("https://api.spacex.land/graphql/")

//        Gql.init(
//            "https://graphql.anilist.co/",
//            OkHttpClient().newBuilder()
//                .addInterceptor(HttpLoggingInterceptor().apply {
//                    setLevel(HttpLoggingInterceptor.Level.BODY)
//                })
//                .build()
//        )

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
                .get<Data>(
                    onSuccess = {
                        withContext(Dispatchers.Main) {
                            println(it.data)
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

data class Data(
    val data: ArtsyResponse? = null
)

data class ArtsyResponse(
    val popular_artists: PopularArtists? = null
)

data class PopularArtists(
    val artists: List<Artist> = emptyList()
)

data class Artist(
    val name: String
)