package rs.etf.snippet.rest.ktor.models

import kotlinx.serialization.Serializable

@Serializable
data class Obligation(
    var id: Int,
    var isDone: Boolean,
    val informalName: String,
    val dateTime: String,
    val description: String
)

var ephemeralObligations = mutableListOf<Obligation>()