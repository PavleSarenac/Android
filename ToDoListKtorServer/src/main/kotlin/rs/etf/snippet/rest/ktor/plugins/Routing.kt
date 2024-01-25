package rs.etf.snippet.rest.ktor.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import rs.etf.snippet.rest.ktor.routes.obligationRouting

fun Application.configureRouting() {
    routing {
        obligationRouting()
    }
}
