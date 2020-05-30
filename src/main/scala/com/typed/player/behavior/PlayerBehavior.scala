package com.typed.player.behavior

import akka.actor.typed.Behavior
import com.typed.player.behavior.protocol.PlayerCommands.PlayerCommand

private trait PlayerBehavior[QC <: PlayerCommand] {

  def receiveMessage(cmd: QC): Behavior[PlayerCommand]
}
