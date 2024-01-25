package rs.ac.bg.etf.jokes.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Url
import rs.ac.bg.etf.jokes.data.retrofit.models.Joke
import rs.ac.bg.etf.jokes.data.retrofit.models.MultipleJokes

const val BASE_URL = "https://v2.jokeapi.dev/joke/"

interface JokesApi {
    @GET
    suspend fun getOneJoke(@Url url: String): Joke

    @GET
    suspend fun getMultipleJokes(@Url url: String): MultipleJokes
}
