package com.typed.player.behavior.protocol

import akka.actor.typed.ActorRef
import PlayerReplies._
import com.typed.player.models.Track

object PlayerCommands {

  sealed trait PlayerCommand
  sealed trait MiddleTrackCommand extends PlayerCommand
  sealed trait EmptyCommand extends PlayerCommand
  sealed trait FirstTrackCommand extends PlayerCommand
  sealed trait LastTrackCommand extends PlayerCommand
  sealed trait OnlyTrackCommand extends PlayerCommand

  case class TogglePlay(replyTo: ActorRef[PlayToggled])
    extends MiddleTrackCommand
    with OnlyTrackCommand
    with FirstTrackCommand
    with LastTrackCommand

  case class ToggleShuffle(replyTo: ActorRef[ShuffleToggled])
    extends MiddleTrackCommand
    with EmptyCommand
    with OnlyTrackCommand
    with FirstTrackCommand
    with LastTrackCommand

  case class Skip(replyTo: ActorRef[SkippedReply])
    extends MiddleTrackCommand
    with FirstTrackCommand

  case class SkipBack(replyTo: ActorRef[SkippedBackReply])
    extends MiddleTrackCommand
    with LastTrackCommand

  case class EnqueueFirstTrack(track: Track, replyTo: ActorRef[FirstTrackEnqueued])
    extends EmptyCommand

  case class EnqueueSecondTrack(track: Track, replyTo: ActorRef[SecondTrackEnqueued])
    extends OnlyTrackCommand

  case class EnqueueTrack(track: Track, replyTo: ActorRef[TrackEnqueuedReply])
    extends MiddleTrackCommand
    with FirstTrackCommand
    with LastTrackCommand

  case class Stop(replyTo: ActorRef[Stopped])
    extends MiddleTrackCommand
    with OnlyTrackCommand
    with FirstTrackCommand
    with LastTrackCommand
}
