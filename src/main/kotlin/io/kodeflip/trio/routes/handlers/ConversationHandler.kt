package io.kodeflip.trio.routes.handlers

import io.kodeflip.trio.domain.Clients
import io.kodeflip.trio.domain.Conversations
import io.kodeflip.trio.ext.withStandardFallbacks
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class ConversationHandler(
  private val clients: Clients,
  private val conversations: Conversations
) {

  fun getByClient(request: ServerRequest): Mono<ServerResponse> =
    request.toMono()
      .flatMap { clients.findById(it.pathVariable("client")) }
      .flatMap { conversations.findByClientId(it.id!!) }
      .flatMap { ok().json().bodyValue(it) }
      .withStandardFallbacks()
}
