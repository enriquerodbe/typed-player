package com.typed.player.player_interface.state

import akka.actor.typed.ActorRef
import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.queue.QueueCommands
import com.typed.player.queue.QueueCommands.PlayingCommand

case class PlayingState(
    queue: ActorRef[PlayingCommand]) extends InterfaceState[PlayingCommand] {

  override val translator: Translator = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case Skip(_) => QueueCommands.Skip
    case SkipBack(_) => QueueCommands.SkipBack
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }
}
