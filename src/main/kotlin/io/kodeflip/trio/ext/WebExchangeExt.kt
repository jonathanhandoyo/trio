package io.kodeflip.trio.ext

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.json
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

/**
 * Switches to `Mono.error<T>()` signal when predicate evaluates to `true`.
 *
 * @param T type of incoming signal
 * @param predicate predicate to evaluate
 *
 * @return passes element of type T when predicate evaluates to `false`
 */
fun <T> Mono<T>.errorIf(predicate: (T) -> Boolean): Mono<T> = handle { t, sink ->
  when (predicate.invoke(t)) {
    true -> sink.error(RuntimeException())
    else -> sink.next(t)
  }
}

private class ForbiddenException : ResponseStatusException(HttpStatus.FORBIDDEN)
private class UnauthorizedException : ResponseStatusException(HttpStatus.UNAUTHORIZED)

fun forbidden() = status(HttpStatus.FORBIDDEN)
fun internalServerError() = status(HttpStatus.INTERNAL_SERVER_ERROR)
fun unauthorized() = status(HttpStatus.UNAUTHORIZED)

/**
 * On `ForbiddenException`, resumes building the `ServerResponse` with status 403 and JSON-ified exception as its body
 */
fun Mono<ServerResponse>.withForbiddenFallback() = onErrorResume(ForbiddenException::class) {
  forbidden()
    .json()
    .bodyValue(it)
}

/**
 * On exception, resumes building the `ServerResponse` with status 500 and JSON-ified exception as its body
 */
fun Mono<ServerResponse>.withInternalServerErrorFallback() = onErrorResume {
  internalServerError()
    .json()
    .bodyValue(it)
}

/**
 * On empty, resumes building the `ServerResponse` with status 404 and empty body
 */
fun Mono<ServerResponse>.withNotFoundFallback() = switchIfEmpty {
  notFound()
    .build()
}

/**
 * On `UnauthorizedException`, resumes building the `ServerResponse` with status 401 and JSON-ified exception as its body
 */
fun Mono<ServerResponse>.withUnauthorizedFallback() = onErrorResume(UnauthorizedException::class) {
  unauthorized()
    .json()
    .bodyValue(it)
}

/**
 * Compounds standard execution strategy for non-positive results. Includes:
 * * Fallback for `401-Unauthorized` response
 * * Fallback for `403-Forbidden` response
 * * Fallback for `404-NotFound` response
 * * Fallback for `500-InternalServerError` response
 */
fun Mono<ServerResponse>.withStandardFallbacks(): Mono<ServerResponse> =
  this
    .withUnauthorizedFallback() //401
    .withForbiddenFallback() //403
    .withNotFoundFallback() //404
    .withInternalServerErrorFallback() //500

fun ServerResponse.BodyBuilder.text() = this.contentType(MediaType.TEXT_PLAIN)