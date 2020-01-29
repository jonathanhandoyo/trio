package io.kodeflip.trio.routes

import io.kodeflip.trio.ext.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class Routes(
  private val filters: Filters,
  private val mappings: Mappings
) {

  companion object {
    private val logger = getLogger<Routes>()
  }

  @Bean
  fun api(): RouterFunction<ServerResponse> {
    return RouterFunctions.route()
      .add(mappings.clients())
      .add(mappings.webhooks())
      .filter(filters.logging())
      .build()
  }
}
