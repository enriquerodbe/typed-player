package com.typed.player.player_interface

import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.queue.QueueCommands

private object Translators {

  val Stopped: Translator[QueueCommands.StoppedCommand] = {
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle(_)
    case EnqueueTrack(track, _) => QueueCommands.EnqueueFirstTrack(track, _)
  }

  val OnlyTrack: Translator[QueueCommands.PlayingOnlyTrackCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueSecondTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  val FirstTrack: Translator[QueueCommands.PlayingFirstTrackCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case Skip(_) => QueueCommands.Skip
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  val Playing: Translator[QueueCommands.PlayingCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case Skip(_) => QueueCommands.Skip
    case SkipBack(_) => QueueCommands.SkipBack
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  val LastTrack: Translator[QueueCommands.PlayingLastTrackCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case SkipBack(_) => QueueCommands.SkipBack
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }
}
