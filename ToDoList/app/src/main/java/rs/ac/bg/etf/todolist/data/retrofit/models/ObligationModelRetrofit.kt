package rs.ac.bg.etf.todolist.data.retrofit.models

data class ObligationModelRetrofit(
    val id: Int,
    val isDone: Boolean,
    val informalName: String,
    val dateTime: String,
    val description: String
)