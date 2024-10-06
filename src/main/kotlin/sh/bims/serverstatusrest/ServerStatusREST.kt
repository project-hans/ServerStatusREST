package sh.bims.serverstatusrest


import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

class ServerStatusREST : ModInitializer {
    private var server: MinecraftServer? = null
    var SERVER_NODE: String = System.getenv("SERVER_NODE") ?: "main"
    var SERVER_NAME: String = System.getenv("SERVER_NAME") ?: "server"




    override fun onInitialize() {
        // Register server lifecycle event
        ServerLifecycleEvents.SERVER_STARTED.register(ServerLifecycleEvents.ServerStarted { minecraftServer ->
            server = minecraftServer
            startKtorServer()
        })
    }

    private fun startKtorServer() {
        embeddedServer(Netty, port = 42069) {
            install(ContentNegotiation) {
                gson()
            }
            routing {
                get("/playercount") {
                    val playerCount = server?.currentPlayerCount
                    call.respond(mapOf("playerCount" to playerCount))
                }
                get("/maxplayercount") {
                    val maxplayerCount = server?.maxPlayerCount
                    call.respond(mapOf("maxplayerCount" to maxplayerCount))
                }
                get("/nodetype") {
                    call.respond(mapOf("nodetype" to SERVER_NODE))
                }
                get("/status") {
                    call.respond(HttpStatusCode.OK)
                }
                get("/servername") {
                    call.respond(mapOf("server_name" to SERVER_NAME))
                }
                get("/coffee") {
                    call.respond(HttpStatusCode.fromValue(418))
                }
                post("/softstop"){
                    println("Stop signal received, shutting server down")
                    call.respond(HttpStatusCode.OK)
                    server?.stop(true)
                }
            }
        }.start(wait = false)

    }



}
