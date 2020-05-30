package com.typed.player.player_interface.protocol

import com.typed.player.models.Player

object PlayerInterfaceReplies {

  sealed trait Reply
  case class Ok(player: Player) extends Reply
  case class Error(message: String) extends Reply
}
