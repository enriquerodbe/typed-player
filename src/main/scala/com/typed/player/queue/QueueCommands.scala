package com.typed.player.queue

import akka.actor.typed.ActorRef
import com.typed.player.models.Track
import com.typed.player.queue.QueueReplies._

object QueueCommands {

  sealed trait QueueCommand
  sealed trait PlayingCommand extends QueueCommand
  sealed trait StoppedCommand extends QueueCommand
  sealed trait PlayingFirstTrackCommand extends QueueCommand
  sealed trait PlayingLastTrackCommand extends QueueCommand
  sealed trait PlayingOnlyTrackCommand extends QueueCommand

  case class TogglePlay(replyTo: ActorRef[PlayToggled])
    extends PlayingCommand
    with PlayingOnlyTrackCommand
    with PlayingFirstTrackCommand
    with PlayingLastTrackCommand

  case class ToggleShuffle(replyTo: ActorRef[ShuffleToggled])
    extends PlayingCommand
    with StoppedCommand
    with PlayingOnlyTrackCommand
    with PlayingFirstTrackCommand
    with PlayingLastTrackCommand

  case class Skip(replyTo: ActorRef[SkippedReply])
    extends PlayingCommand
    with PlayingFirstTrackCommand

  case class SkipBack(replyTo: ActorRef[SkippedBackReply])
    extends PlayingCommand
    with PlayingLastTrackCommand

  case class EnqueueFirstTrack(track: Track, replyTo: ActorRef[FirstTrackEnqueued])
    extends StoppedCommand

  case class EnqueueSecondTrack(track: Track, replyTo: ActorRef[SecondTrackEnqueued])
    extends PlayingOnlyTrackCommand

  case class EnqueueTrack(track: Track, replyTo: ActorRef[TrackEnqueuedReply])
    extends PlayingCommand
    with PlayingFirstTrackCommand
    with PlayingLastTrackCommand

  case class Stop(replyTo: ActorRef[Stopped])
    extends PlayingCommand
    with PlayingOnlyTrackCommand
    with PlayingFirstTrackCommand
    with PlayingLastTrackCommand
}
