package com.typed.player.player_interface

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.player_interface.PlayerInterfaceCommands.Command
import com.typed.player.player_interface.state._
import com.typed.player.queue.StoppedQueue

object PlayerInterface {

  def apply(): Behavior[Command] = Behaviors.setup { ctx =>
    val queue = ctx.spawn(StoppedQueue(), "queue")
    StoppedState(queue).behavior
  }
}
