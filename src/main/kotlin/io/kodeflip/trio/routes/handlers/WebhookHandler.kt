package io.kodeflip.trio.routes.handlers

import io.kodeflip.trio.domain.RocketMessage
import io.kodeflip.trio.ext.getLogger
import io.kodeflip.trio.ext.withStandardFallbacks
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono

@Component
class WebhookHandler {

  companion object {
    private val logger = getLogger<WebhookHandler>()
  }

  fun messageSent(request: ServerRequest): Mono<ServerResponse> {
    return request.bodyToMono<RocketMessage>()
      .flatMap { ok().json().bodyValue(it) }
      .withStandardFallbacks()
  }

  fun userCreated(request: ServerRequest): Mono<ServerResponse> {
    return request.bodyToMono<RocketMessage>()
      .flatMap { ok().json().bodyValue(it) }
      .withStandardFallbacks()
  }
}
