package rs.ac.bg.etf.todolist.data.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import rs.ac.bg.etf.todolist.data.retrofit.models.ObligationModelRetrofit

const val BASE_URL = "http://192.168.0.38:8080/obligations/"

interface ObligationApi {
    @POST("addNewObligation")
    suspend fun addNewObligation(@Body obligation: ObligationModelRetrofit)

    @GET("getAllObligations")
    suspend fun getAllObligations(): List<ObligationModelRetrofit>

    @GET("getObligation")
    suspend fun getObligation(@Query("obligationId") obligationId: Int): ObligationModelRetrofit

    @GET("markObligationAsDone")
    suspend fun markObligationAsDone(@Query("obligationId") obligationId: Int)
}