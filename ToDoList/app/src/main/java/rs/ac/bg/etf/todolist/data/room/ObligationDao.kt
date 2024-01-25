package rs.ac.bg.etf.todolist.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ObligationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addObligations(obligations: List<ObligationModelRoom>)

    @Query("SELECT * FROM obligation")
    fun getAllObligations(): Flow<List<ObligationModelRoom>>

    @Query("SELECT * FROM obligation WHERE id = :obligationId LIMIT 1")
    suspend fun getObligation(obligationId: Int): ObligationModelRoom

    @Query("UPDATE obligation SET isDone = :isDone WHERE id = :obligationId")
    suspend fun markObligationAsDone(obligationId: Int, isDone: Boolean = true)
}