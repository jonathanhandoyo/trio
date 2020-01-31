package io.kodeflip.trio.ext

import reactor.core.publisher.Mono

/**
 * Switches to `Mono.error<T>()` signal when predicate evaluates to `true`.
 */
fun <T> Mono<T>.errorWhen(predicate: (T) -> Boolean): Mono<T> {
  return handle { t, sink ->
    when (predicate.invoke(t)) {
      true -> sink.error(RuntimeException())
      else -> sink.next(t)
    }
  }
}
