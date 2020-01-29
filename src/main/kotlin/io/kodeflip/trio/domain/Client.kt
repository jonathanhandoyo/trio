package io.kodeflip.trio.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Document
data class Client(
  @Id
  val id: String?,
  val name: String,
  val active: Boolean,
  val provider: Provider
) : Actor

@Repository
interface Clients: ReactiveMongoRepository<Client, String>
