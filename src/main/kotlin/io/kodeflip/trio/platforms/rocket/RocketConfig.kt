package io.kodeflip.trio.platforms.rocket

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "rocket")
class RocketConfig {
  lateinit var url: String

  lateinit var userId: String
  lateinit var authToken: String
}
