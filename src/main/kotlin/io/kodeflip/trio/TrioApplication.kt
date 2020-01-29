package io.kodeflip.trio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrioApplication

fun main(args: Array<String>) {
	runApplication<TrioApplication>(*args)
}
