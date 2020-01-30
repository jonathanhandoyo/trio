package io.kodeflip.trio.platforms.rocket

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class RocketHealthIndicator(private val rocket: Rocket): ReactiveHealthIndicator {

  override fun health(): Mono<Health> {
    return rocket.getInfo()
      .map { Health.up().withDetail("info", it).build() }
      .onErrorResume { Health.down().withException(it).build().toMono() }
  }

}
