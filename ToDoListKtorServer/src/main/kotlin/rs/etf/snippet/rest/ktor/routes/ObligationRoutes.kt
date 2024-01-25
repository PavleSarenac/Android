package rs.etf.snippet.rest.ktor.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import rs.etf.snippet.rest.ktor.models.Obligation
import rs.etf.snippet.rest.ktor.models.ephemeralObligations

var nextObligationId = 1

fun Route.obligationRouting() {
    route("obligations") {
        post("addNewObligation") {
            val newObligation = call.receive<Obligation>()
            newObligation.id = nextObligationId++
            ephemeralObligations.add(newObligation)
            call.respondText(
                "New obligation has been successfully stored on the ktor server.",
                status = HttpStatusCode.Created
            )
        }

        get("getAllObligations") {
            call.respond(ephemeralObligations)
        }

        get("getObligation") {
            val obligationId = call.request.queryParameters["obligationId"]?.toInt() ?: 1
            call.respond(ephemeralObligations.filter { it.id == obligationId }[0])
        }

        get("markObligationAsDone") {
            val obligationId = call.request.queryParameters["obligationId"]?.toInt() ?: 1
            ephemeralObligations.filter { it.id == obligationId }[0].isDone = true
            call.respondText(
                "Obligation was successfully marked as done.",
                status = HttpStatusCode.OK
            )
        }
    }
}
