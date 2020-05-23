package com.typed.player.queue

import akka.actor.typed.ActorRef
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._

object QueueReplies {

  sealed trait Reply {
    def state: Queue
  }

  case class PlayToggled(state: Queue) extends Reply

  case class ShuffleToggled(state: Queue) extends Reply

  sealed trait SkippedReply extends Reply
  case class SkippedFromFirst(
      state: Queue, next: ActorRef[PlayingCommand]) extends SkippedReply
  case class Skipped(state: Queue) extends SkippedReply
  case class SkippedToLastTrack(
      state: Queue, next: ActorRef[PlayingLastTrackCommand]) extends SkippedReply

  sealed trait SkippedBackReply extends Reply
  case class SkippedBackFromLastTrack(
      state: Queue, next: ActorRef[PlayingCommand]) extends SkippedBackReply
  case class SkippedBack(state: Queue) extends SkippedBackReply
  case class SkippedBackToFirst(
      state: Queue, next: ActorRef[PlayingFirstTrackCommand]) extends SkippedBackReply

  case class FirstTrackEnqueued(state: Queue, next: ActorRef[PlayingOnlyTrackCommand]) extends Reply
  case class SecondTrackEnqueued(
      state: Queue, next: ActorRef[PlayingFirstTrackCommand]) extends Reply
  sealed trait TrackEnqueuedReply extends Reply
  case class TrackEnqueuedAfterLast(
      state: Queue, next: ActorRef[PlayingCommand]) extends TrackEnqueuedReply
  case class TrackEnqueued(state: Queue) extends TrackEnqueuedReply

  case class Stopped(state: Queue, next: ActorRef[StoppedCommand]) extends Reply
}
