package sh.bims.serverstatusrest;

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking

data class ServerResponse(val servername: String)

public class CustomHttpClient {
    var OPERATOR_URL: String =System.getenv("OPERATOR_URL") ?: "localhost:6969/servername"
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    fun getServerName(): String? = runBlocking {
        try {
            val response: ServerResponse = client.get(OPERATOR_URL).body()
            response.servername
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
