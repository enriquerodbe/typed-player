package com.reactive.player.queue

import akka.actor.typed.Behavior
import com.reactive.player.models.Queue
import com.reactive.player.queue.QueueCommands._

object QueueReplies {

  sealed trait Reply {
    def state: Queue
  }

  case class PlayToggled(state: Queue) extends Reply

  case class ShuffleToggled(state: Queue) extends Reply

  sealed trait SkippedReply extends Reply
  case class SkippedFromFirst(next: Behavior[PlayingCommand], state: Queue) extends SkippedReply
  case class Skipped(state: Queue) extends SkippedReply
  case class SkippedToLastTrack(
      next: Behavior[PlayingLastTrackCommand],
      state: Queue) extends SkippedReply

  sealed trait SkippedBackReply extends Reply
  case class SkippedBackFromLastTrack(
      next: Behavior[PlayingCommand], state: Queue) extends SkippedBackReply
  case class SkippedBack(state: Queue) extends SkippedBackReply
  case class SkippedBackToFirst(
      next: Behavior[PlayingFirstTrackCommand],
      state: Queue) extends SkippedBackReply

  case class FirstTrackEnqueued(next: Behavior[PlayingOnlyTrackCommand], state: Queue) extends Reply
  case class SecondTrackEnqueued(
      next: Behavior[PlayingFirstTrackCommand],
      state: Queue) extends Reply
  sealed trait TrackEnqueuedReply extends Reply
  case class TrackEnqueuedAfterLast(
      next: Behavior[PlayingCommand],
      state: Queue) extends TrackEnqueuedReply
  case class TrackEnqueued(state: Queue) extends TrackEnqueuedReply

  case class Stopped(state: Queue) extends Reply
}
