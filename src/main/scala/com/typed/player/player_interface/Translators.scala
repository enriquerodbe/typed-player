package com.typed.player.player_interface

import com.typed.player.behavior.protocol.PlayerCommands
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands._

private object Translators {

  val Stopped: Translator[PlayerCommands.StoppedCommand] = {
    case ToggleShuffle(_) => PlayerCommands.ToggleShuffle(_)
    case EnqueueTrack(track, _) => PlayerCommands.EnqueueFirstTrack(track, _)
  }

  val OnlyTrack: Translator[PlayerCommands.OnlyTrackCommand] = {
    case TogglePlay(_) => PlayerCommands.TogglePlay
    case ToggleShuffle(_) => PlayerCommands.ToggleShuffle
    case EnqueueTrack(track, _) => PlayerCommands.EnqueueSecondTrack(track, _)
    case Stop(_) => PlayerCommands.Stop
  }

  val FirstTrack: Translator[PlayerCommands.FirstTrackCommand] = {
    case TogglePlay(_) => PlayerCommands.TogglePlay
    case ToggleShuffle(_) => PlayerCommands.ToggleShuffle
    case Skip(_) => PlayerCommands.Skip
    case EnqueueTrack(track, _) => PlayerCommands.EnqueueTrack(track, _)
    case Stop(_) => PlayerCommands.Stop
  }

  val MiddleTrack: Translator[PlayerCommands.MiddleTrackCommand] = {
    case TogglePlay(_) => PlayerCommands.TogglePlay
    case ToggleShuffle(_) => PlayerCommands.ToggleShuffle
    case Skip(_) => PlayerCommands.Skip
    case SkipBack(_) => PlayerCommands.SkipBack
    case EnqueueTrack(track, _) => PlayerCommands.EnqueueTrack(track, _)
    case Stop(_) => PlayerCommands.Stop
  }

  val LastTrack: Translator[PlayerCommands.LastTrackCommand] = {
    case TogglePlay(_) => PlayerCommands.TogglePlay
    case ToggleShuffle(_) => PlayerCommands.ToggleShuffle
    case SkipBack(_) => PlayerCommands.SkipBack
    case EnqueueTrack(track, _) => PlayerCommands.EnqueueTrack(track, _)
    case Stop(_) => PlayerCommands.Stop
  }
}
