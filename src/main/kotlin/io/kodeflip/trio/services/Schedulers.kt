package io.kodeflip.trio.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kodeflip.trio.domain.AppleMessage
import io.kodeflip.trio.domain.Message
import io.kodeflip.trio.domain.WechatMessage
import io.kodeflip.trio.ext.getLogger
import io.kodeflip.trio.platforms.dmz.Dmz
import io.kodeflip.trio.platforms.dmz.DmzPayload
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class Schedulers(
  private val dmz: Dmz,
  private val messaging: Messaging,

  private val mapper: ObjectMapper
) {

  companion object {
    val logger = getLogger<Schedulers>()
  }

  @Scheduled(fixedDelay = 1000)
  fun poll() {
    dmz
      .getSingle()
      .handle<Message> { payload, sink ->
        val url = payload.url
        when {
          url.startsWith("/sc/bc") -> sink.next(Message(mapper.readValue<AppleMessage>(payload.body)))
          url.startsWith("/sc/wc") -> sink.next(Message(mapper.readValue<WechatMessage>(payload.body)))
          else -> logger.warn(">> Schedulers >> unprocessable [{}]", payload)
        }
      }
      .flatMap { messaging.process(it) }
      .subscribe()
  }
}
