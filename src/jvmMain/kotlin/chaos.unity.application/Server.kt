package chaos.unity.application

import io.ktor.http.HttpStatusCode
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
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        script(src = "/static/reverco.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::myApplicationModule).start(wait = true)
}

fun Application.myApplicationModule() {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        post("/submit")  {
            val bytecode = call.receive<ByteArray>().map { it.toInt() and 0xFF }

            println(bytecode.slice(0 until 4))
            println(bytecode.slice(0 until 4) == arrayOf(0xCA, 0xFE, 0xBA, 0xBE))
            println(bytecode[0] == 0xCA)
            println(bytecode[0])
            println(0xCA)

            call.respondRedirect("/success")
        }
        get("/success") {
            call.respondHtml {
                body {
                    h1 {
                        +"KEK"
                    }
                }
            }
        }
        static("/static") {
            resources()
        }
    }
}