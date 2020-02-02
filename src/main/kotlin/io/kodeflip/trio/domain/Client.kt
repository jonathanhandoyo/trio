package io.kodeflip.trio.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Document
data class Client(
  @Id
  val id: String?,
  val name: String,
  val active: Boolean,
  val provider: Provider,

  val conversation: Ref?
) : Actor

@Repository
interface Clients: ReactiveMongoRepository<Client, String> {
  fun findByProviderApple(apple: String): Mono<Client>
  fun findByProviderWechat(wechat: String): Mono<Client>

  fun findByConversationId(conversationId: String): Mono<Client>
}
