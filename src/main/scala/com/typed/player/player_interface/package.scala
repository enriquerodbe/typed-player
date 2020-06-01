package com.typed.player

import akka.actor.typed.ActorRef
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands.Request
import com.typed.player.behavior.protocol.PlayerCommands.PlayerCommand
import com.typed.player.behavior.protocol.PlayerReplies

package object player_interface {

  private[player_interface] type PlayerCommandConstructor[C <: PlayerCommand] =
    ActorRef[PlayerReplies.Reply] => C

  private[player_interface] type Translator[C <: PlayerCommand] =
    PartialFunction[Request, PlayerCommandConstructor[C]]
}
