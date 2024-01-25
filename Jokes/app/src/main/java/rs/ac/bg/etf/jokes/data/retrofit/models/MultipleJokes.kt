package rs.ac.bg.etf.jokes.data.retrofit.models

data class MultipleJokes(
    val error: Boolean = false,
    val amount: Int = 2,
    val jokes: List<Joke> = listOf()
)