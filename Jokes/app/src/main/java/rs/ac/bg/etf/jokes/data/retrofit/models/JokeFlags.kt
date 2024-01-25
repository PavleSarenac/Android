package rs.ac.bg.etf.jokes.data.retrofit.models

data class JokeFlags(
    val nsfw: Boolean = false,
    val religious: Boolean = false,
    val political: Boolean = false,
    val racist: Boolean = false,
    val sexist: Boolean = false,
    val explicit: Boolean = false
)