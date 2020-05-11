package com.typed.player.player_interface.state

import akka.actor.typed.{ActorRef, Behavior}
import com.typed.player.player_interface.PlayerInterfaceCommands.Request
import com.typed.player.queue.QueueCommands.QueueCommand
import com.typed.player.queue.QueueReplies

trait InterfaceState[QC <: QueueCommand] {

  type QueueCommandConstructor = ActorRef[QueueReplies.Reply] => QC
  type Translator = PartialFunction[Request, QueueCommandConstructor]

  def queueBehavior: Behavior[QC]
  def translator: Translator
}
