package io.kodeflip.trio.ext

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

fun <T> T?.toMonoOrEmpty(): Mono<T> = Mono.justOrEmpty(this)
fun <T> T?.toFluxOrEmpty(): Flux<T> = Mono.justOrEmpty(this).flux()

fun <T> Mono<T>.withEmptyOnError(): Mono<T> = onErrorResume { Mono.empty() }

fun <T1, T2> Mono<T1>.pairWhen(other: (T1) -> Mono<T2>): Mono<Pair<T1, T2>> = this.zipWhen { t1 -> other(t1) }.map { (t1, t2) -> t1 to t2 }
