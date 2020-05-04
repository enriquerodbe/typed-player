package com.reactive.player.protocols.queue

import akka.actor.typed.Behavior
import com.reactive.player.protocols.queue.QueueCommands._

object QueueReplies {

  sealed trait Reply

  case class PlayToggled(status: Boolean) extends Reply

  case class ShuffleToggled(status: Boolean) extends Reply

  sealed trait SkippedReply extends Reply
  case class Skipped(next: Behavior[PlayingCommand]) extends SkippedReply
  case object Skipped extends SkippedReply
  case class SkippedToLastTrack(next: Behavior[PlayingLastTrackCommand]) extends SkippedReply

  sealed trait SkippedBackReply extends Reply
  case class SkippedBack(next: Behavior[PlayingCommand]) extends SkippedBackReply
  case object SkippedBack extends SkippedBackReply
  case class SkippedToFirstTrack(next: Behavior[PlayingFirstTrackCommand]) extends SkippedBackReply

  case class FirstTrackEnqueued(next: Behavior[PlayingOnlyTrackCommand]) extends Reply
  case class SecondTrackEnqueued(next: Behavior[PlayingFirstTrackCommand]) extends Reply
  sealed trait TrackEnqueuedReply extends Reply
  case class TrackEnqueued(next: Behavior[PlayingCommand]) extends TrackEnqueuedReply
  case object TrackEnqueued extends TrackEnqueuedReply

  case object Stopped extends Reply
}
