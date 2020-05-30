package com.typed.player

import akka.actor.typed.ActorRef
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands.Request
import com.typed.player.behavior.protocol.PlayerCommands.PlayerCommand
import com.typed.player.behavior.protocol.PlayerReplies

package object player_interface {

  private[player_interface] type PlayerCommandConstructor[PC <: PlayerCommand] =
    ActorRef[PlayerReplies.Reply] => PC

  private[player_interface] type Translator[PC <: PlayerCommand] =
    PartialFunction[Request, PlayerCommandConstructor[PC]]
}
