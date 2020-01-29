package io.kodeflip.trio.ext

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T: Any> getLogger(): Logger = LoggerFactory.getLogger(T::class.java)
