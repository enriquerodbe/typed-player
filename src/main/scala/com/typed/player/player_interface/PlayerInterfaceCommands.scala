package com.typed.player.player_interface

import akka.actor.typed.ActorRef
import com.typed.player.models.Track
import com.typed.player.player_interface.PlayerInterfaceReplies.Reply
import com.typed.player.queue.QueueReplies

object PlayerInterfaceCommands {

  sealed trait Command {
    def replyTo: ActorRef[Reply]
  }

  sealed trait Request extends Command
  case class TogglePlay(replyTo: ActorRef[Reply]) extends Request
  case class ToggleShuffle(replyTo: ActorRef[Reply]) extends Request
  case class Skip(replyTo: ActorRef[Reply]) extends Request
  case class SkipBack(replyTo: ActorRef[Reply]) extends Request
  case class EnqueueTrack(track: Track, replyTo: ActorRef[Reply]) extends Request
  case class Stop(replyTo: ActorRef[Reply]) extends Request

  private[player_interface] case class QueueReply(
      reply: QueueReplies.Reply,
      replyTo: ActorRef[Reply]) extends Command
}
