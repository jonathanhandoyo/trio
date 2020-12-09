package io.kodeflip.trio.platforms.rocket

import io.kodeflip.trio.ext.errorIf
import io.kodeflip.trio.ext.lift
import io.kodeflip.trio.platforms.Platform
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicBoolean

@Service
class Rocket(
  private val config: RocketConfig
): Platform {

  override val client: WebClient = WebClient.create(config.url)
  override val health: AtomicBoolean = AtomicBoolean(true)

  fun getInfo(): Mono<RcInfo> =
    client
      .get()
      .uri("/api/v1/info")
      .headers { headers ->
        headers.set("Accept", "application/json")
      }
      .retrieve()
      .bodyToMono<Map<String, Any>>()
      .errorIf { it["success"] != true }
      .map { it.lift<RcInfo>("info") }
      .withHealthSwitch()

  fun getUserInfo(userId: String): Mono<RcUser> =
    client
      .get()
      .uri { builder ->
        builder
          .path("/api/v1/users.info")
          .queryParam("userId", userId)
          .build()
      }
      .headers { headers ->
        headers.set("Accept", "application/json")
        headers.set("X-User-Id", config.userId)
        headers.set("X-Auth-Token", config.authToken)
      }
      .retrieve()
      .bodyToMono<Map<String, Any>>()
      .errorIf { it["success"] != true }
      .map { it.lift<RcUser>("user") }
      .withHealthSwitch()
}
