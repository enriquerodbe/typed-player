package com.typed.player.player_interface.protocol

import akka.actor.typed.ActorRef
import com.typed.player.behavior.protocol.PlayerReplies
import com.typed.player.models.Track
import PlayerInterfaceReplies.Reply

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

  private[player_interface] case class PlayerReply(
      reply: PlayerReplies.Reply,
      replyTo: ActorRef[Reply]) extends Command
}
