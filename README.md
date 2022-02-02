## Android lightweight Graphql library

An alternative network library for executing queries or mutations from a GraphQL server in a simple way. Built on top of OkHttp makes this lib is customizable and easy to use.

This lib takes care of every single use-case from GraphQL requests easily and is ready to go for getting the data safely. As simple as creating a Gql builder and listen to the response!

### Notes
- It uses OkHttp and already supported HTTP/2
- Coroutine-first, the return value is suspend function

***

### Setup

Add this in your deps
```gradle
implementation('app.isfaaghyth.graphql:1.0.0')
```

Don't forget to add the internet permission on AndroidManifest if you haven't yet
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### How-to

Initializing the Lib in `onCreate()` of Application class
```kt
Gql.init("https://gql.isfa.xyz/api")
```

as it uses OkHttp, you can customize the OkHttpClient while initializing it like this
```kt
val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

Gql.init("https://gql.isfa.xyz/api", okHttpClient)
```

Make your first simple request!
```kt
val query = """
        query user_info {
            userInfo {
                id
                name
            }
        }
    """.trimIndent()
Gql.query(query)
    .get<UserInfo>(
        onSuccess = {
        },
        onError = {
        }
    )
```

A request with parameters
```kt
val query = """
        query user_info($id: String, $token: Int, $is_active: Boolean) {
            userInfo {
                id
                name
            }
        }
    """.trimIndent()
Gql.query(query)
    .parameters(mapOf(
        "id" to "user-123-xyz",
        "token" to 123456789,
        "is_active" to true
    ))
    .get<UserInfo>(
        onSuccess = {
        },
        onError = {
        }
    )
```