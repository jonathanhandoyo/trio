package io.kodeflip.trio.platforms.dmz

import org.springframework.http.HttpHeaders

data class DmzPayload(
  val url: String,
  val headers: HttpHeaders,
  val body: String
)
