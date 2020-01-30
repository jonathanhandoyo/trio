package io.kodeflip.trio.platforms.rocket

import com.fasterxml.jackson.annotation.JsonProperty

data class RcInfo(
  val version: String
)

data class RcUser(
  @JsonProperty("_id")
  val id: String,
  val type: String,
  val active: Boolean,
  val name: String,
  val username: String
)
