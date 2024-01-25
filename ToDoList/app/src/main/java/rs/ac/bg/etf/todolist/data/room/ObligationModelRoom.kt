package rs.ac.bg.etf.todolist.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "obligation")
data class ObligationModelRoom(
    @PrimaryKey val id: Int,
    @ColumnInfo var isDone: Boolean,
    @ColumnInfo val informalName: String,
    @ColumnInfo val dateTime: String
)