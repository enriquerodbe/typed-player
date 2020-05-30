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
      state: Queue, replyTo: ActorRef[PlayingCommand]) extends SkippedReply
  case class Skipped(state: Queue) extends SkippedReply
  case class SkippedToLastTrack(
      state: Queue, replyTo: ActorRef[PlayingLastTrackCommand]) extends SkippedReply

  sealed trait SkippedBackReply extends Reply
  case class SkippedBackFromLastTrack(
      state: Queue, replyTo: ActorRef[PlayingCommand]) extends SkippedBackReply
  case class SkippedBack(state: Queue) extends SkippedBackReply
  case class SkippedBackToFirst(
      state: Queue, replyTo: ActorRef[PlayingFirstTrackCommand]) extends SkippedBackReply

  case class FirstTrackEnqueued(
      state: Queue, replyTo: ActorRef[PlayingOnlyTrackCommand]) extends Reply
  case class SecondTrackEnqueued(
      state: Queue, replyTo: ActorRef[PlayingFirstTrackCommand]) extends Reply
  sealed trait TrackEnqueuedReply extends Reply
  case class TrackEnqueuedAfterLast(
      state: Queue, replyTo: ActorRef[PlayingCommand]) extends TrackEnqueuedReply
  case class TrackEnqueued(state: Queue) extends TrackEnqueuedReply

  case class Stopped(state: Queue, replyTo: ActorRef[StoppedCommand]) extends Reply
}
