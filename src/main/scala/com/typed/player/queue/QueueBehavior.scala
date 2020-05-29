package com.typed.player.queue

import akka.actor.typed.Behavior
import com.typed.player.queue.QueueCommands.QueueCommand

private trait QueueBehavior[QC <: QueueCommand] {

  def receiveMessage(cmd: QC): Behavior[QueueCommand]
}
