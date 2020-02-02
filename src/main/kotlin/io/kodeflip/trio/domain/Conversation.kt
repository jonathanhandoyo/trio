package io.kodeflip.trio.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Document
data class Conversation(
  @Id
  val id: String?,
  val client: Client,
  val provider: Provider,

  val rms: List<Manager>,
  val arms: List<Manager>?,
  val tls: List<Manager>?,
  val ics: List<Manager>?,
  val dacs: List<Manager>?
) {
  fun toRef(): Ref = Ref(id!!, provider)
}

@Repository
interface Conversations: ReactiveMongoRepository<Conversation, String> {
  fun findByClientId(clientId: String): Mono<Conversation>
  fun findByProviderRocket(rocket: String): Mono<Conversation>
}
