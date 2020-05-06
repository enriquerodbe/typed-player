package com.reactive.player.player_interface.state

import akka.actor.typed.Behavior
import com.reactive.player.player_interface.PlayerInterfaceCommands._
import com.reactive.player.queue.QueueCommands
import com.reactive.player.queue.QueueCommands.PlayingOnlyTrackCommand

case class OnlyTrackState(queueBehavior: Behavior[PlayingOnlyTrackCommand])
  extends InterfaceState[PlayingOnlyTrackCommand] {

  override val translator: Translator = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueSecondTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }
}
