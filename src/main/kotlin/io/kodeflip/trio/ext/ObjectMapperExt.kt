package io.kodeflip.trio.ext

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue

/**
 * Assumes an input of `Map<String, Any>>`, retrieves a sub-map from given key, converts to the reified parameter of `T`
 *
 * @param map: any nested Map construct
 * @param key: key within the Map to lift
 *
 * @return lifted sub-map converted to `T`
 *
 * @throws IllegalStateException when the `key` is not present
 */
inline fun <reified T> ObjectMapper.liftAndConvertValue(map: Map<String, Any>, key: String) = this.convertValue<T>(map[key] ?: error("key not found"))
