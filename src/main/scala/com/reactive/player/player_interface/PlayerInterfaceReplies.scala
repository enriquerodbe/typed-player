package com.reactive.player.player_interface

import com.reactive.player.models.Queue

object PlayerInterfaceReplies {

  sealed trait Reply
  case class Ok(queue: Queue) extends Reply
  case class Error(message: String) extends Reply
}
