package rs.ac.bg.etf.jokes.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rs.ac.bg.etf.jokes.data.retrofit.BASE_URL
import rs.ac.bg.etf.jokes.data.retrofit.JokesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JokesModule {

    @Singleton
    @Provides
    fun provideJokesApi(): JokesApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(JokesApi::class.java)
    }

}