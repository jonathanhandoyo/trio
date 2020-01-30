package io.kodeflip.trio.platforms.rocket

import com.fasterxml.jackson.databind.ObjectMapper
import io.kodeflip.trio.ext.convertInnerValue
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class Rocket(
  private val config: RocketConfig,
  private val mapper: ObjectMapper
) {

  private val client: WebClient = WebClient.create(config.url)

  fun getInfo(): Mono<RcInfo> {
    return client
      .get()
      .uri("/api/v1/info")
      .retrieve()
      .bodyToMono<Map<String, Any>>()
      .map { mapper.convertInnerValue<RcInfo>(it, "info") }
  }

  fun getUserInfo(): Mono<RcUser> {
    return client
      .get()
      .uri { builder ->
        builder
          .path("/api/v1/users.info")
          .queryParam("userId", config.userId)
          .build()
      }
      .headers { headers ->
        headers.set("Accept", "application/json")
        headers.set("X-User-Id", config.userId)
        headers.set("X-Auth-Token", config.authToken)
      }
      .retrieve()
      .bodyToMono<Map<String, Any>>()
      .map { mapper.convertInnerValue<RcUser>(it, "user") }
  }
}
