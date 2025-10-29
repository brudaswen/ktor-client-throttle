package throttle

import de.brudaswen.ktor.client.throttle.HttpRequestThrottle
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTimedValue
import io.ktor.server.cio.CIO as ServerCIO

internal class ThrottleIntegrationTest {

    private val server = embeddedServer(ServerCIO, port = 8080) {
        install(RateLimit) {
            global {
                rateLimiter(
                    limit = 5,
                    refillPeriod = 2.seconds,
                )
            }
        }

        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }

    @Test
    fun `client should not fail if refillPeriod is large enough`() = runBlocking {
        server.startSuspend()

        val client = HttpClient(CIO) {
            expectSuccess = true

            install(HttpRequestThrottle) {
                throttler(
                    limit = 5,
                    refillPeriod = 3.seconds,
                    retry = false,
                )
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.HEADERS
            }
        }
        val stats = List(10) { it }.map { id ->
            measureTimedValue {
                client.get("http://localhost:8080/") {
                    parameter("id", id)
                }.status
            }.let { (status, duration) ->
                "Request $id took $duration: $status"
            }
        }

        println(stats.joinToString("\n"))

        server.stopSuspend()
    }

    @Test
    fun `client should retry failed 429 requests`() = runBlocking {
        server.startSuspend()

        val client = HttpClient(CIO) {
            expectSuccess = true

            install(HttpRequestThrottle) {
                throttler(
                    limit = 5,
                    refillPeriod = 2.seconds,
                    retry = true,
                )
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.HEADERS
            }
        }
        val stats = List(10) { it }.map { id ->
            measureTimedValue {
                client.get("http://localhost:8080/") {
                    parameter("id", id)
                }.status
            }.let { (status, duration) ->
                "Request $id took $duration: $status"
            }
        }

        println(stats.joinToString("\n"))

        server.stopSuspend()
    }
}
