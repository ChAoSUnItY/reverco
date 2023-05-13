package chaos.unity.application

import chaos.unity.application.bytecode.serializeClass
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Reverco")
    }
    body {
        script(src = "/static/reverco.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::revercoModule).start(wait = true)
}

fun Application.revercoModule() {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        post("/submit")  {
            println(call.request.headers["Content-Type"])

            if (call.request.headers["Content-Type"] == "application/octet-stream") {
                val bytecode = call.receive<ByteArray>()
                val classStructure = serializeClass(bytecode)

                if (classStructure != null) {
                    call.respond(HttpStatusCode.OK, classStructure)
                }
            }

            call.respondText("Invalid data", status = HttpStatusCode.Forbidden)
        }
        static("/static") {
            resources()
        }
    }
}