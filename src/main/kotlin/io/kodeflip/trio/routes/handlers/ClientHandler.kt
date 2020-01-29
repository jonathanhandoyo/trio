package io.kodeflip.trio.routes.handlers

import io.kodeflip.trio.domain.Clients
import io.kodeflip.trio.ext.withInternalServerError
import io.kodeflip.trio.ext.withNotFound
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
    return clients.findAll().collectList()
      .flatMap { ok().json().bodyValue(it) }
      .withNotFound()
      .withInternalServerError()
  }

  fun getById(request: ServerRequest): Mono<ServerResponse> {
    return clients.findById(request.pathVariable("client"))
      .flatMap { ok().json().bodyValue(it) }
      .withNotFound()
      .withInternalServerError()
  }

  fun deactivateById(request: ServerRequest): Mono<ServerResponse> {
    return clients.findById(request.pathVariable("client"))
      .map { it.copy(active = false) }
      .flatMap { clients.save(it) }
      .flatMap { ok().json().bodyValue(it) }
      .withNotFound()
      .withInternalServerError()
  }
}
