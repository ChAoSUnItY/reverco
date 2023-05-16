import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val client = HttpClient { 
    install(ContentNegotiation) {
        json()
    }
}

suspend fun requestClassFileParsing(className: String, bytes: List<Byte>): ClassStructure {
    val response = client.post("/parse") {
        contentType(ContentType.Application.Json)
        setBody(ClassFileData(className, bytes))
    }

    return response.body<ClassStructure>()
}
