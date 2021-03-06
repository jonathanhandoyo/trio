package io.kodeflip.trio.services

import io.kodeflip.trio.domain.*
import io.kodeflip.trio.ext.pairWhen
import io.kodeflip.trio.platforms.rocket.RcMessage
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class Messaging(
  private val clients: Clients,
  private val conversations: Conversations,
  private val managers: Managers,
  private val messages: Messages
) {

  fun process(message: Message): Mono<Boolean> =
    when (message.source) {
      Message.Platform.APPLE -> message.processIncoming()
      Message.Platform.ROCKET -> message.processOutgoing()
      Message.Platform.WECHAT -> message.processOutgoing()
    }


  private fun Message.processOutgoing(): Mono<Boolean> =
    this.toMono()
      .save()
      .populateManagerAsSender()
      .populateConversationAsTarget()
      .thenReturn(true)

  private fun Message.processIncoming(): Mono<Boolean> =
    this.toMono()
      .save()
      .populateClientAsSender()
      .populateClientAsTarget()
      .thenReturn(true)

  private fun Mono<Message>.save(): Mono<Message> = flatMap {
    messages.save(it)
  }

  private fun Mono<Message>.populateClientAsSender(): Mono<Message> = flatMap { message ->
    val client: Mono<Client> =
      when (val original = message.original) {
        is AppleMessage -> clients.findByProviderApple(original.id)
        is WechatMessage -> clients.findByProviderWechat(original.id)
        else -> RuntimeException().toMono()
      }

    client.map { message.copy(sender = it) }
  }

  private fun Mono<Message>.populateManagerAsSender(): Mono<Message> = flatMap { message ->
    managers
      .findByProviderRocket((message.original as RcMessage).sentBy.id)
      .map { message.copy(sender = it) }
  }

  private fun Mono<Message>.populateConversationAsTarget(): Mono<Pair<Message, Conversation>> = pairWhen { message ->
    val client = message.sender as Client
    conversations.findByClientId(client.id!!)
  }

  private fun Mono<Message>.populateClientAsTarget(): Mono<Pair<Message, Client>> = pairWhen { message ->
    val original = message.original as RcMessage
    conversations
      .findByProviderRocket(original.roomId)
      .flatMap { clients.findByConversationId(it.id!!) }
  }
}
