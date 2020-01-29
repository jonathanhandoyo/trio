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

fun Mono<ServerResponse>.withInternalServerError() = this.onErrorResume { internalServerError().json().bodyValue(it) }
fun Mono<ServerResponse>.withNotFound() = this.switchIfEmpty { notFound().build() }
