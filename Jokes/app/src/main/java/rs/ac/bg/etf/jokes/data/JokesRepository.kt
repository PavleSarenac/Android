package rs.ac.bg.etf.jokes.data

import rs.ac.bg.etf.jokes.data.retrofit.JokesApi
import javax.inject.Inject

class JokesRepository @Inject constructor(
    private val jokesApi: JokesApi,
) {
    suspend fun getOneJoke(url: String) = jokesApi.getOneJoke(url)

    suspend fun getMultipleJokes(url: String) = jokesApi.getMultipleJokes(url)
}

