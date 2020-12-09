package io.kodeflip.trio.routes

import io.kodeflip.trio.ext.forbidden
import io.kodeflip.trio.ext.getLogger
import io.kodeflip.trio.ext.text
import io.kodeflip.trio.ext.unauthorized
import io.kodeflip.trio.routes.handlers.ClientHandler
import io.kodeflip.trio.routes.handlers.ConversationHandler
import io.kodeflip.trio.routes.handlers.WebhookHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.time.Instant

@Configuration
class Routes(
  private val client: ClientHandler,
  private val conversation: ConversationHandler,
  private val webhook: WebhookHandler
) {

  companion object {
    private val logger = getLogger<Routes>()
  }

  fun onBefore(request: ServerRequest): ServerRequest {
    request.attributes()["X-Request-Timestamp"] = Instant.now().toEpochMilli()
    return request
  }

  fun onAfter(request: ServerRequest, response: ServerResponse): ServerResponse {
    val span = Instant.now().toEpochMilli() - (request.attributes()["X-Request-Timestamp"] as Long)
    logger.info(">> Response [origin: ${request.remoteAddressOrNull()}][uri: ${request.uri()}][span: $span ms]")
    return response
  }

  fun onUnauthorized(request: ServerRequest, function: (ServerRequest) -> Mono<ServerResponse>): Mono<ServerResponse> {
    return when (request.headers().header("X-Trio-Requestor").contains("jonathan.handoyo")) {
      true -> function(request)
      else -> unauthorized().build()
    }
  }

  fun onForbidden(request: ServerRequest, function: (ServerRequest) -> Mono<ServerResponse>): Mono<ServerResponse> {
    return when (request.headers().header("X-Trio-Requestor").isNotEmpty()) {
      true -> function(request)
      else -> forbidden().build()
    }
  }

  fun clients() = router {
    "/clients".nest {
      logger.info(">> Route configured [/clients]")
      GET(client::getAll)
      POST(client::onboardClient)
      PUT(client::onboardPlatform)

      "/{client}".nest {
        logger.info(">> Route configured [/clients/{}]")
        GET(client::getById)
        DELETE(client::deactivateById)

        "/conversation".nest {
          logger.info(">> Route configured [/clients/{}/conversation]")
          GET(conversation::getByClient)
        }
      }
    }
  }

  fun webhooks() = router {
    "/webhooks".nest {
      logger.info(">> Route configured [/webhook]")
      POST("message-sent", webhook::messageSent)
      POST("user-created", webhook::userCreated)
    }
  }

  @Bean
  fun api(): RouterFunction<ServerResponse> = router {
    before { request -> onBefore(request) }

    filter { request, function -> onUnauthorized(request, function) }
    filter { request, function -> onForbidden(request, function) }

    GET { ok().text().bodyValue("hello") }

    clients()
    webhooks()

    after { request, response -> onAfter(request, response) }
  }
}
