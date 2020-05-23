package com.typed.player.player_interface.state

import akka.actor.typed.ActorRef
import com.typed.player.player_interface.PlayerInterfaceCommands.{EnqueueTrack, ToggleShuffle}
import com.typed.player.queue.QueueCommands
import com.typed.player.queue.QueueCommands.StoppedCommand

case class StoppedState(queue: ActorRef[StoppedCommand]) extends InterfaceState[StoppedCommand] {

  override val translator: Translator = {
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueFirstTrack(track, _)
  }
}
