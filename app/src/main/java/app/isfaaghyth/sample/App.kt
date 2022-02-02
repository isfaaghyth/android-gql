package app.isfaaghyth.sample

import android.app.Application
import app.isfaaghyth.graphql.Gql
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Gql.init(
            BASE_URL,
            OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
                .build()
        )
    }

    companion object {
        private const val BASE_URL = "https://api.spacex.land/graphql/"
    }

}