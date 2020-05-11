package com.typed.player.player_interface.state

import akka.actor.typed.Behavior
import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.queue.QueueCommands
import com.typed.player.queue.QueueCommands.PlayingOnlyTrackCommand

case class OnlyTrackState(queueBehavior: Behavior[PlayingOnlyTrackCommand])
  extends InterfaceState[PlayingOnlyTrackCommand] {

  override val translator: Translator = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueSecondTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }
}
