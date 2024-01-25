package rs.ac.bg.etf.jokes.data.retrofit.models

data class Joke(
    val error: Boolean = false,
    val category: String = "",
    val type: String = "",
    val joke: String? = null,
    val setup: String? = null,
    val delivery: String? = null,
    val flags: JokeFlags = JokeFlags(),
    val id: Int = 0,
    val safe: Boolean = true,
    val lang: String = ""
)