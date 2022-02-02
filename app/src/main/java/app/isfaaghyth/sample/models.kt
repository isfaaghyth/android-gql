package app.isfaaghyth.sample

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