package io.kodeflip.trio.ext

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

fun forbidden() = status(HttpStatus.FORBIDDEN)
fun internalServerError() = status(HttpStatus.INTERNAL_SERVER_ERROR)
fun unauthorized() = status(HttpStatus.UNAUTHORIZED)

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
      notFound()
        .build()
    }
}
