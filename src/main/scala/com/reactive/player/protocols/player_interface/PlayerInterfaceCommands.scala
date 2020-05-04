package com.reactive.player.protocols.player_interface

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.ActorContext
import com.reactive.player.models.Track
import com.reactive.player.protocols.player_interface.PlayerInterfaceReplies.Reply
import com.reactive.player.protocols.queue.QueueReplies

object PlayerInterfaceCommands {

  sealed trait Command {
    def replyTo: ActorRef[Reply]

    def createAdapter(implicit ctx: ActorContext[Command]): ActorRef[QueueReplies.Reply] = {
      ctx.messageAdapter(QueueReply(_, replyTo))
    }
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
