package io.kodeflip.trio.platforms.dmz

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "dmz")
class DmzConfig {
  lateinit var url: String
  lateinit var polling: String
}
