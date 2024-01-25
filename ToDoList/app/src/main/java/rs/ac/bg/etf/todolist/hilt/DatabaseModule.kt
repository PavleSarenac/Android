package rs.ac.bg.etf.todolist.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import rs.ac.bg.etf.todolist.data.room.ObligationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(coroutineScope: CoroutineScope, @ApplicationContext context: Context) =
        ObligationDatabase.getDatabase(context, coroutineScope)

    @Provides
    @Singleton
    fun providesObligationDao(database: ObligationDatabase) =
        database.obligationDao()
}