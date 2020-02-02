package io.kodeflip.trio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableConfigurationProperties
@EnableScheduling
@SpringBootApplication
class TrioApplication

fun main(args: Array<String>) {
  runApplication<TrioApplication>(*args)
}
