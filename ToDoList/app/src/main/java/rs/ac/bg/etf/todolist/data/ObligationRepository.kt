package rs.ac.bg.etf.todolist.data

import rs.ac.bg.etf.todolist.data.retrofit.ObligationApi
import rs.ac.bg.etf.todolist.data.retrofit.models.ObligationModelRetrofit
import rs.ac.bg.etf.todolist.data.room.ObligationDao
import rs.ac.bg.etf.todolist.data.room.ObligationModelRoom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObligationRepository @Inject constructor(
    private val obligationApi: ObligationApi,
    private val obligationDao: ObligationDao
) {
    val allObligations = obligationDao.getAllObligations()

    // ktor
    suspend fun addNewObligationToKtorServer(obligation: ObligationModelRetrofit) =
        obligationApi.addNewObligation(obligation)

    suspend fun getAllObligationsFromKtorServer(): List<ObligationModelRetrofit> =
        obligationApi.getAllObligations()

    suspend fun getObligationFromKtorServer(obligationId: Int) =
        obligationApi.getObligation(obligationId)

    // room
    suspend fun addObligationsToLocalDatabase(obligations: List<ObligationModelRoom>) =
        obligationDao.addObligations(obligations)

    suspend fun getObligationFromLocalDatabase(obligationId: Int) =
        obligationDao.getObligation(obligationId)

    // ktor + room
    suspend fun markObligationAsDoneRoom(obligationId: Int) {
        obligationApi.markObligationAsDone(obligationId)
        obligationDao.markObligationAsDone(obligationId)
    }
}