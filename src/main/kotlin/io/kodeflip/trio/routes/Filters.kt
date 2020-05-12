package io.kodeflip.trio.routes

import io.kodeflip.trio.ext.getLogger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.remoteAddressOrNull
import reactor.core.publisher.Mono
import java.time.Instant

@Component
class Filters {
  private val logger = getLogger<Filters>()

  fun logging(): (ServerRequest, HandlerFunction<ServerResponse>) -> Mono<ServerResponse> {
    logger.info(">> Filter configured [Logging]")
    return { request, next ->
      val origin = request.remoteAddressOrNull()
      val uri = request.uri()
      val start = Instant.now()

      logger.info(">> Req [origin: $origin][uri: $uri]")
      next
        .handle(request)
        .doOnTerminate {
          val time = Instant.now().toEpochMilli() - start.toEpochMilli()
          logger.info("<< Res [origin: $origin][uri: $uri][time: $time ms]")
        }
    }
  }
}
