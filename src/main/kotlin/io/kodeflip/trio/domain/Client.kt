package io.kodeflip.trio.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Client(
  @Id
  val id: String?,
  val name: String,
  val provider: Provider
) : Actor
