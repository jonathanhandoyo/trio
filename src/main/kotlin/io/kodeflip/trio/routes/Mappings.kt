package io.kodeflip.trio.routes

import io.kodeflip.trio.ext.getLogger
import io.kodeflip.trio.routes.handlers.ClientHandler
import io.kodeflip.trio.routes.handlers.WebhookHandler
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router

@Component
class Mappings(
  private val client: ClientHandler,
  private val webhook: WebhookHandler
) {

  companion object {
    private val logger = getLogger<Mappings>()
  }

  fun clients() = router {
    "/clients".nest {
      logger.info(">> Route configured [/clients]")
      GET("", client::getAll)

      "/{client}".nest {
        logger.info(">> Route configured [/clients/{}]")
        GET("", client::getById)
        DELETE("", client::deactivateById)
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
}
