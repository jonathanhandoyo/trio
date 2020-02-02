package io.kodeflip.trio.platforms.dmz

import io.kodeflip.trio.ext.getLogger
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicBoolean

@Service
class Dmz(private val config: DmzConfig) {

  companion object {
    val logger = getLogger<Dmz>()
  }

  private val client: WebClient = WebClient.create(config.url)
  private val ok: AtomicBoolean = AtomicBoolean(true)

  private fun <T> Mono<T>.withEmptyOnError(): Mono<T> {
    return onErrorResume {
      logger.error("{} : {}", it.javaClass, it.message)
      Mono.empty<T>()
    }
  }

  private fun <T> Mono<T>.withHealthSwitcher(): Mono<T> {
    return this
      .doOnNext { ok.set(true) }
      .doOnError { ok.set(false) }
  }

  fun isOk(): Mono<Boolean> = ok.get().toMono()
  fun isPolling(): Boolean = config.polling.toBoolean()

  fun getSingle(): Mono<DmzPayload> {
    return client
      .post()
      .uri("/dequeue")
      .retrieve()
      .bodyToMono<DmzPayload>()
      .withHealthSwitcher()
      .withEmptyOnError()
  }
}
