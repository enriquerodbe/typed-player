package com.typed.player.player_interface.state

import akka.actor.typed.Behavior
import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.queue.QueueCommands
import com.typed.player.queue.QueueCommands.PlayingLastTrackCommand

case class LastTrackState(queueBehavior: Behavior[PlayingLastTrackCommand])
  extends InterfaceState[PlayingLastTrackCommand] {

  override val translator: Translator = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case SkipBack(_) => QueueCommands.SkipBack
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }
}
