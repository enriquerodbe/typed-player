package com.typed.player

import akka.actor.typed.ActorRef
import com.typed.player.player_interface.PlayerInterfaceCommands.Request
import com.typed.player.queue.QueueCommands.QueueCommand
import com.typed.player.queue.QueueReplies

package object player_interface {

  private[player_interface] type QueueCommandConstructor[QC <: QueueCommand] =
    ActorRef[QueueReplies.Reply] => QC

  private[player_interface] type Translator[QC <: QueueCommand] =
    PartialFunction[Request, QueueCommandConstructor[QC]]
}
