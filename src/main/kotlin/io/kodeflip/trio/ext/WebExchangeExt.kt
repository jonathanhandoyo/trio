package io.kodeflip.trio.ext

import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

/**
 * Switches to `Mono.error<T>()` signal when predicate evaluates to `true`.
 */
fun <T> Mono<T>.errorIf(predicate: (T) -> Boolean): Mono<T> {
  return handle { t, sink ->
    when (predicate.invoke(t)) {
      true -> sink.error(RuntimeException())
      else -> sink.next(t)
    }
  }
}



/**
 * On exception, resumes building the `ServerResponse` with status 500 and JSON-ified exception as its body
 */
fun Mono<ServerResponse>.withInternalServerError(): Mono<ServerResponse> {
  return this
    .onErrorResume {
      internalServerError()
        .json()
        .bodyValue(it)
    }
}

/**
 * On empty, resumes building the `ServerResponse` with status 404 and empty body
 */
fun Mono<ServerResponse>.withNotFound(): Mono<ServerResponse> {
  return this
    .switchIfEmpty {
      ServerResponse.notFound()
        .build()
    }
}

fun Mono<ServerResponse>.withStandardFallbacks(): Mono<ServerResponse> {
  return this
    .withNotFound()
    .withInternalServerError()
}
