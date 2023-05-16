package chaos.unity.application

import ClassFileData
import chaos.unity.application.bytecode.serializeClass
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.Identity.decode
import kotlinx.html.*
import kotlinx.serialization.json.Json

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
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        route("/parse") {
            accept(ContentType.Application.Json) {
                post {
                    try {
                        val data = call.receive<ClassFileData>()

                        println(data)

                        val classStructure = serializeClass(data.fileName, data.bytes.toByteArray())

                        println(classStructure)

                        if (classStructure != null) {
                            call.respond(HttpStatusCode.OK, classStructure)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        static("/static") {
            resources()
        }
    }
}