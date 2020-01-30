package io.kodeflip.trio.ext

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue

inline fun <reified T> ObjectMapper.convertInnerValue(map: Map<String, Any>, key: String) = this.convertValue<T>(map[key] ?: error("key not found"))
