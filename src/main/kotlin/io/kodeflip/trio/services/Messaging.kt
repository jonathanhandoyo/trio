package io.kodeflip.trio.services

import io.kodeflip.trio.domain.*
import io.kodeflip.trio.platforms.rocket.RcMessage
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class Messaging(
  private val clients: Clients,
  private val managers: Managers,
  private val messages: Messages
) {

  fun process(message: Message): Mono<Boolean> {
    return when (message.source) {
      Message.Platform.APPLE -> processIncoming(message)
      Message.Platform.ROCKET -> processOutgoing(message)
      Message.Platform.WECHAT -> processOutgoing(message)
    }
  }



  private fun Mono<Message>.save(): Mono<Message> {
    return flatMap { messages.save(it) }
  }

  private fun Mono<Message>.populateClientAsSender(): Mono<Message> {
    return flatMap { message ->
      val client: Mono<Client> = when (val original = message.original) {
        is AppleMessage -> clients.findByProviderApple(original.id)
        is WechatMessage -> clients.findByProviderWechat(original.id)
        else -> RuntimeException().toMono()
      }

      client.map { message.copy(sender = it) }
    }
  }

  private fun Mono<Message>.populateManagerAsSender(): Mono<Message> {
    return flatMap { message ->
      val original: RcMessage = message.original as RcMessage
      managers
        .findByProviderRocket(original.sentBy.id)
        .map { message.copy(sender = it) }
    }
  }

  private fun processOutgoing(message: Message): Mono<Boolean> {
    return message.toMono()
      .save()
      .populateManagerAsSender()
      .thenReturn(true)
  }

  private fun processIncoming(message: Message): Mono<Boolean> {
    return message.toMono()
      .save()
      .populateClientAsSender()
      .thenReturn(true)
  }
}
