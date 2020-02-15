package io.kodeflip.trio.ext

import reactor.core.publisher.Mono

infix fun <T1, T2> Mono<T1>.zip(other: Mono<T2>) = Mono.zip(this, other)

fun <T> Mono<T>.withEmptyOnError(): Mono<T> = onErrorResume { Mono.empty<T>() }


