package io.kodeflip.trio.platforms.dmz

import io.kodeflip.trio.ext.withEmptyOnError
import io.kodeflip.trio.platforms.Platform
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicBoolean

@Service
class Dmz(private val config: DmzConfig): Platform {

  override val client: WebClient = WebClient.create(config.url)
  override val health: AtomicBoolean = AtomicBoolean(true)

  fun isHealthy(): Mono<Boolean> = health.get().toMono()
  fun isPolling(): Boolean = config.polling.toBoolean()

  fun getSingle(): Mono<DmzPayload> {
    return client
      .post()
      .uri("/dequeue")
      .retrieve()
      .bodyToMono<DmzPayload>()
      .withHealthSwitch()
      .withEmptyOnError()
  }
}
