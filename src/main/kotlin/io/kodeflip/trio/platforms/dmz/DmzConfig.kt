package io.kodeflip.trio.platforms.dmz

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "dmz")
data class DmzConfig(
  val url: String,
  val polling: String
)
