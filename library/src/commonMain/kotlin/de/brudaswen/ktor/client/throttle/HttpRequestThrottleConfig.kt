package de.brudaswen.ktor.client.throttle

import io.ktor.http.HttpStatusCode.Companion.TooManyRequests
import io.ktor.utils.io.KtorDsl
import kotlin.time.Duration

/**
 * [HttpRequestThrottle] Ktor Client Plugin configuration.
 */
@KtorDsl
public class HttpRequestThrottleConfig {
    /**
     * The [HttpRequestThrottler] used for request throttling.
     */
    public var throttler: HttpRequestThrottler = DefaultHttpRequestThrottler()

    /**
     * The default [HttpRequestThrottler] does not throttle and only takes care of automatic
     * retries. If the server responds with `429` [TooManyRequests] and the `Retry-After` header,
     * then the original request is automatically retried according to the delay defined in the
     * header.
     *
     * @param retry If `true` and the server responds with [TooManyRequests], then the request is
     * automatically retried according to the `Retry-After` header if the server sends this
     * information.
     */
    public fun throttler(
        retry: Boolean = true,
    ) {
        throttler = DefaultHttpRequestThrottler(
            retry = retry,
        )
    }

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
    public fun throttler(
        limit: Int,
        refillPeriod: Duration,
        retry: Boolean = true,
    ) {
        throttler = DefaultHttpRequestThrottler(
            limit = limit,
            refillPeriod = refillPeriod,
            retry = retry,
        )
    }
}
