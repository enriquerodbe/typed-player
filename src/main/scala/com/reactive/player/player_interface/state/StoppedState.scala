package com.reactive.player.player_interface.state

import akka.actor.typed.Behavior
import com.reactive.player.player_interface.PlayerInterfaceCommands.{EnqueueTrack, ToggleShuffle}
import com.reactive.player.queue.QueueCommands.StoppedCommand
import com.reactive.player.queue.{QueueCommands, StoppedQueue}

object StoppedState extends InterfaceState[StoppedCommand] {

  override val translator: Translator = {
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueFirstTrack(track, _)
  }

  override val queueBehavior: Behavior[StoppedCommand] = StoppedQueue()
}
