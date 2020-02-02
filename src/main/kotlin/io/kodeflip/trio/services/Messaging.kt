package io.kodeflip.trio.services

import io.kodeflip.trio.domain.*
import io.kodeflip.trio.platforms.rocket.RcMessage
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple2

@Service
class Messaging(
  private val clients: Clients,
  private val conversations: Conversations,
  private val managers: Managers,
  private val messages: Messages
) {

  fun process(message: Message): Mono<Boolean> {
    return when (message.source) {
      Message.Platform.APPLE -> message.processIncoming()
      Message.Platform.ROCKET -> message.processOutgoing()
      Message.Platform.WECHAT -> message.processOutgoing()
    }
  }



  private fun Message.processOutgoing(): Mono<Boolean> {
    return this.toMono()
      .save()
      .populateManagerAsSender()
      .populateConversationAsTarget()
      .thenReturn(true)
  }

  private fun Message.processIncoming(): Mono<Boolean> {
    return this.toMono()
      .save()
      .populateClientAsSender()
      .populateClientAsTarget()
      .thenReturn(true)
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

  private fun Mono<Message>.populateConversationAsTarget(): Mono<Tuple2<Message, Conversation>> {
    return zipWhen { message ->
      val client = message.sender as Client
      conversations.findByClientId(client.id!!)
    }
  }

  private fun Mono<Message>.populateClientAsTarget(): Mono<Tuple2<Message, Client>> {
    return zipWhen { message ->
      val original = message.original as RcMessage
      conversations
        .findByProviderRocket(original.roomId)
        .flatMap { clients.findByConversationId(it.id!!) }
    }
  }
}
