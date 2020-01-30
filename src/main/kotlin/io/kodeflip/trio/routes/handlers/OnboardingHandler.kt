package io.kodeflip.trio.routes.handlers

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class OnboardingHandler {

  fun onboardClient(request: ServerRequest): Mono<ServerResponse> = ok().build()
  fun onboardPlatform(request: ServerRequest): Mono<ServerResponse> = ok().build()
}
