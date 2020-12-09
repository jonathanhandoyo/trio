package io.kodeflip.trio.services

import io.kodeflip.trio.domain.Client
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class Onboarding {

  fun onboard(client: Client): Mono<Client> = client.toMono()
}
