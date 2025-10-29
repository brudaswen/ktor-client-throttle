package de.brudaswen.ktor.client.throttle

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay

/**
 * Throttle logic for the [HttpRequestThrottle] Ktor Client Plugin.
 *
 * Can throttle client requests in case the server is rate-limited.
 *
 * Kind of the counterpart to the
 * [Ktor Rate Limiting Server Plugin](https://ktor.io/docs/server-rate-limit.html).
 */
public interface HttpRequestThrottler {
    /**
     * Throttle this [request] by calling [delay] or return immediately if this
     * request should not be throttled.
     */
    public suspend fun throttle(request: HttpRequestBuilder)

    /**
     * Handle the [HttpResponse] which allows to update internal state (e.g. by evaluating server
     * response headers.
     *
     * This method can return `true` to retry the last request again.
     *
     * @return Return `true`
     */
    public suspend fun onResponse(response: HttpResponse): Boolean
}
