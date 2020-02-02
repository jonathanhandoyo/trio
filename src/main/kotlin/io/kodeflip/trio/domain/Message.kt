package io.kodeflip.trio.domain

import io.kodeflip.trio.platforms.rocket.RcMessage
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Document
data class Message(
  @Id
  val id: String,
  val source: Platform,
  val original: Any,
  val sender: Actor?
) {
  enum class Platform {
    APPLE,
    ROCKET,
    WECHAT
  }

  constructor(rocket: RcMessage): this(rocket.id, Platform.ROCKET, rocket, null)

  constructor(apple: AppleMessage): this(apple.id, Platform.APPLE, apple, null)
  constructor(wechat: WechatMessage): this(wechat.id, Platform.WECHAT, wechat, null)
}

@Repository
interface Messages: ReactiveMongoRepository<Message, String>

data class AppleMessage(val id: String)
data class WechatMessage(val id: String)
