package de.brudaswen.ktor.client.throttle

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode.Companion.TooManyRequests
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

/**
 * Default [HttpRequestThrottler] that starts with [limit] tokens, and will be refilled every
 * [refillPeriod].
 *
 * If the server responds with `429` [TooManyRequests] and the `Retry-After` header, then the
 * original request is automatically retried according to the delay defined in the header.
 * This functionality can be disabled via the [retry] flag.
 *
 * @param limit The number of tokens.
 * @param refillPeriod Period after which the tokens get refilled.
 * @param retry If `true` and the server responds with [TooManyRequests], then the request is
 * automatically retried according to the `Retry-After` header if the server sends this
 * information.
 */
internal class DefaultHttpRequestThrottler(
    private var limit: Int = 1,
    private val refillPeriod: Duration = Duration.ZERO,
    private val retry: Boolean = true,
) : HttpRequestThrottler {

    private val mutex = Mutex()

    private var remaining = 0

    private var reset: Instant = Instant.DISTANT_PAST

    override suspend fun throttle(request: HttpRequestBuilder) {
        mutex.withLock {
            if (refillPeriod > Duration.ZERO) {
                // Throttle if all slots are blocked
                if (remaining <= 0) {
                    delay(reset - Clock.System.now())

                    // Refill bucket if reset time has passed
                    reset = Clock.System.now() + refillPeriod
                    remaining = limit
                }

                // Block one slot and continue this request
                remaining--
            }
        }
    }

    override suspend fun onResponse(response: HttpResponse): Boolean =
        retry && response.shouldRetry()

    private suspend fun HttpResponse.shouldRetry(): Boolean =
        when (status) {
            TooManyRequests -> {
                val retryAfter = headers["Retry-After"]
                val retryAfterSeconds = retryAfter?.toLongOrNull()?.seconds

                if (retryAfterSeconds != null) {
                    // The returned `Retry-After` might be too low, due to rounding errors, so why
                    // always add one second to avoid triggering the request to early.
                    // This also ensures that we wait at least one second between multiple retries.
                    delay(retryAfterSeconds + 1.seconds)
                    true
                } else {
                    false
                }
            }

            else -> false
        }
}
