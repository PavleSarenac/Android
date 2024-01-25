package rs.ac.bg.etf.todolist.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rs.ac.bg.etf.todolist.data.retrofit.BASE_URL
import rs.ac.bg.etf.todolist.data.retrofit.ObligationApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ObligationModule {

    @Singleton
    @Provides
    fun provideObligationsApi(): ObligationApi {
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

        return retrofit.create(ObligationApi::class.java)
    }

}