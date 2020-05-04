package com.reactive.player.protocols.player_interface

object PlayerInterfaceReplies {

  sealed trait Reply
  case object Ok extends Reply
  case object Error extends Reply
}
