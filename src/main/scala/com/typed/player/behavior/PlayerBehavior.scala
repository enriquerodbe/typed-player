package com.typed.player.behavior

import akka.actor.typed.Behavior
import com.typed.player.behavior.protocol.PlayerCommands.PlayerCommand

private trait PlayerBehavior[C <: PlayerCommand] {

  def receiveMessage(cmd: C): Behavior[PlayerCommand]
}
