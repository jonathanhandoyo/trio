package io.kodeflip.trio.ext

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val objectMapper = jacksonObjectMapper()

/**
 * Assumes an input of `Map<String, Any>>`, retrieves a sub-map from given key, converts to the reified parameter of `T`
 *
 * @param T reified type of value to be lifted
 * @param key key within the Map to lift
 *
 * @return lifted sub-map converted to value of `T`
 *
 * @throws IllegalStateException when the `key` is not present
 */
inline fun <reified T> Map<String, Any?>.lift(key: String): T =
  objectMapper.convertValue(this[key] ?: throw IllegalArgumentException(""""$key" not present"""))
