package io.kodeflip.trio.routes

import io.kodeflip.trio.ext.getLogger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router

@Component
class Mappings {

  companion object {
    private val logger = getLogger<Mappings>()
  }

  fun clients() = router {  }
  fun webhooks() = router {  }
}
