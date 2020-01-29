package io.kodeflip.trio.domain

data class Message(
  val source: Platform,
  val original: Any,
  val sender: Actor?
) {
  enum class Platform {
    APPLE,
    ROCKET,
    WECHAT
  }

  constructor(apple: AppleMessage): this(Platform.APPLE, apple, null)
  constructor(rocket: RocketMessage): this(Platform.ROCKET, rocket, null)
  constructor(wechat: WechatMessage): this(Platform.WECHAT, wechat, null)
}

data class AppleMessage(val id: String)
data class RocketMessage(val id: String)
data class WechatMessage(val id: String)
