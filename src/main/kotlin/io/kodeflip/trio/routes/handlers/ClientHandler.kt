package io.kodeflip.trio.routes.handlers

import io.kodeflip.trio.domain.Clients
import io.kodeflip.trio.ext.withStandardFallbacks
import io.kodeflip.trio.ext.withStandardFilters
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono

@Component
class ClientHandler(
  private val clients: Clients
) {

  fun getAll(request: ServerRequest): Mono<ServerResponse> {
    return request
      .withStandardFilters()
      .flatMap { clients.findAll().collectList() }
      .flatMap { ok().json().bodyValue(it) }
      .withStandardFallbacks()
  }

  fun getById(request: ServerRequest): Mono<ServerResponse> {
    return request
      .withStandardFilters()
      .flatMap { clients.findById(it.pathVariable("client")) }
      .flatMap { ok().json().bodyValue(it) }
      .withStandardFallbacks()
  }

  fun deactivateById(request: ServerRequest): Mono<ServerResponse> {
    return request
      .withStandardFilters()
      .flatMap { clients.findById(it.pathVariable("client")) }
      .map { it.copy(active = false) }
      .flatMap { clients.save(it) }
      .flatMap { ok().json().bodyValue(it) }
      .withStandardFallbacks()
  }

  fun onboardClient(request: ServerRequest): Mono<ServerResponse> = ok().build()
  fun onboardPlatform(request: ServerRequest): Mono<ServerResponse> = ok().build()
}
