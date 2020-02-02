package io.kodeflip.trio.services

import io.kodeflip.trio.domain.Message
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class Messaging {

  fun process(message: Message): Mono<Boolean> = true.toMono()
}
