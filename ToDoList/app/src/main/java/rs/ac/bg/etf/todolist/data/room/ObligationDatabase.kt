package rs.ac.bg.etf.todolist.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [ObligationModelRoom::class], version = 1, exportSchema = false)
abstract class ObligationDatabase : RoomDatabase() {
    abstract fun obligationDao(): ObligationDao

    companion object {
        private var INSTANCE: ObligationDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ObligationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ObligationDatabase::class.java,
                    "obligation_database"
                ).build()
                INSTANCE = instance
                INSTANCE!!.openHelper.writableDatabase
                return@synchronized instance
            }
        }
    }
}