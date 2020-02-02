package io.kodeflip.trio.ext

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Convenience function to get an instance of `Logger`
 *
 * @param T reified type of the logging class
 *
 * @return constructed `Logger`
 */
inline fun <reified T: Any> getLogger(): Logger = LoggerFactory.getLogger(T::class.java)
