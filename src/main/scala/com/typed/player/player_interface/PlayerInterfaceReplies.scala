package com.typed.player.player_interface

import com.typed.player.models.Queue

object PlayerInterfaceReplies {

  sealed trait Reply
  case class Ok(queue: Queue) extends Reply
  case class Error(message: String) extends Reply
}
