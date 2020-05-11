package com.typed.player.player_interface.state

import akka.actor.typed.Behavior
import com.typed.player.player_interface.PlayerInterfaceCommands.{EnqueueTrack, ToggleShuffle}
import com.typed.player.queue.QueueCommands.StoppedCommand
import com.typed.player.queue.{QueueCommands, StoppedQueue}

object StoppedState extends InterfaceState[StoppedCommand] {

  override val translator: Translator = {
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueFirstTrack(track, _)
  }

  override val queueBehavior: Behavior[StoppedCommand] = StoppedQueue()
}
