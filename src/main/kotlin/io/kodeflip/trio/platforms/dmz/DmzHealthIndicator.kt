package io.kodeflip.trio.platforms.dmz

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class DmzHealthIndicator(private val dmz: Dmz) : ReactiveHealthIndicator {

  override fun health(): Mono<Health> =
    dmz.isHealthy()
      .map {
        when (it) {
          true -> Health.up().withDetail("healthy", it).build()
          else -> Health.down().withDetail("healthy", it).build()
        }
      }
      .onErrorResume { Health.down().withException(it).build().toMono() }
}
