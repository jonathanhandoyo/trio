package io.kodeflip.trio.platforms.rocket

import com.fasterxml.jackson.annotation.JsonProperty

data class RcInfo(
  val version: String
)

data class RcMessage(
  @JsonProperty("_id") val id: String,
  @JsonProperty("rid") val roomId: String,
  @JsonProperty("msg") val text: String,

  @JsonProperty("ts")  val sentAt: Long,
  @JsonProperty("u")   val sentBy: RcUserRef

  /*
  _updatedAt
  editedAt
  editedBy
  urls
  attachments
  alias
  avatar
  groupable
  parseUrls
   */
)

data class RcUser(
  @JsonProperty("_id")
  val id: String,
  val type: String,
  val active: Boolean,
  val name: String,
  val username: String
)

data class RcUserRef(
  @JsonProperty("_id")
  val id: String,
  val username: String
)
