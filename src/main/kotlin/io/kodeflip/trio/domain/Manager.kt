package io.kodeflip.trio.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Document
data class Manager(
  @Id
  val id: String?,
  val name: String,
  val active: Boolean,
  val email: String,
  val provider: Provider
) : Actor {
  fun toRef(): Ref = Ref(id!!, provider)
}

@Repository
interface Managers: ReactiveMongoRepository<Manager, String> {
  fun findByProviderRocket(rocket: String): Mono<Manager>
}
