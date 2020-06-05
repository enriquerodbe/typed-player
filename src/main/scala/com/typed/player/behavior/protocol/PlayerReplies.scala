package com.typed.player.behavior.protocol

import akka.actor.typed.ActorRef
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.models.Player

object PlayerReplies {

  sealed trait Reply {
    def state: Player
  }

  case class PlayToggled(state: Player) extends Reply

  case class ShuffleToggled(state: Player) extends Reply

  sealed trait SkippedReply extends Reply
  case class SkippedFromFirst(
      state: Player, replyTo: ActorRef[MiddleTrackCommand]) extends SkippedReply
  case class Skipped(state: Player) extends SkippedReply
  case class SkippedToLastTrack(
      state: Player, replyTo: ActorRef[LastTrackCommand]) extends SkippedReply

  sealed trait SkippedBackReply extends Reply
  case class SkippedBackFromLastTrack(
      state: Player, replyTo: ActorRef[MiddleTrackCommand]) extends SkippedBackReply
  case class SkippedBack(state: Player) extends SkippedBackReply
  case class SkippedBackToFirst(
      state: Player, replyTo: ActorRef[FirstTrackCommand]) extends SkippedBackReply

  case class FirstTrackEnqueued(
      state: Player, replyTo: ActorRef[OnlyTrackCommand]) extends Reply
  case class SecondTrackEnqueued(
      state: Player, replyTo: ActorRef[FirstTrackCommand]) extends Reply
  sealed trait TrackEnqueuedReply extends Reply
  case class TrackEnqueuedAfterLast(
      state: Player, replyTo: ActorRef[MiddleTrackCommand]) extends TrackEnqueuedReply
  case class TrackEnqueued(state: Player) extends TrackEnqueuedReply

  case class Stopped(state: Player, replyTo: ActorRef[EmptyCommand]) extends Reply
}
