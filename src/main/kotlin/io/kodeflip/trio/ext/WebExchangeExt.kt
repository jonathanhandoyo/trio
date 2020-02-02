package io.kodeflip.trio.ext

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

/**
 * Switches to `Mono.error<T>()` signal when predicate evaluates to `true`.
 */
fun <T> Mono<T>.errorIf(predicate: (T) -> Boolean): Mono<T> =
  handle { t, sink ->
    when (predicate.invoke(t)) {
      true -> sink.error(RuntimeException())
      else -> sink.next(t)
    }
  }


fun forbidden() = status(HttpStatus.FORBIDDEN)
fun internalServerError() = status(HttpStatus.INTERNAL_SERVER_ERROR)
fun unauthorized() = status(HttpStatus.UNAUTHORIZED)

/**
 * On exception, resumes building the `ServerResponse` with status 500 and JSON-ified exception as its body
 */
fun Mono<ServerResponse>.withInternalServerError() =
  onErrorResume {
    internalServerError()
      .json()
      .bodyValue(it)
  }

/**
 * On empty, resumes building the `ServerResponse` with status 404 and empty body
 */
fun Mono<ServerResponse>.withNotFound() =
  switchIfEmpty {
    notFound()
      .build()
  }

/**
 * Compounds standard execution strategy for non-positive results
 */
fun Mono<ServerResponse>.withStandardFallbacks(): Mono<ServerResponse> =
  this
    .withNotFound()
    .withInternalServerError()
