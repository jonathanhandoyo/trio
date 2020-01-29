package io.kodeflip.trio.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

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
)
