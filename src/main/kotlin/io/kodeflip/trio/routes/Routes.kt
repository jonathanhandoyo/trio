package io.kodeflip.trio.routes

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

  @Bean
  fun api(): RouterFunction<ServerResponse> =
    RouterFunctions.route()
      .add(mappings.clients())
      .add(mappings.webhooks())
      .filter(filters.logging())
      .build()
}
