package io.kodeflip.trio.domain

data class Ref(
  val id: String,
  val provider: Provider
)

fun Client.toRef(): Ref = Ref(id!!, provider)
fun Conversation.toRef(): Ref = Ref(id!!, provider)
fun Manager.toRef(): Ref = Ref(id!!, provider)