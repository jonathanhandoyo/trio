package io.kodeflip.trio.platforms

import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicBoolean

interface Platform {

  val client: WebClient
  val health: AtomicBoolean

  fun <T> Mono<T>.withHealthSwitch(): Mono<T> {
    return this
      .doOnNext { health.set(true) }
      .doOnError { health.set(false) }
  }
}
