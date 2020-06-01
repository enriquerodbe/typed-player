package com.typed.player.player_interface

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands.{Command, PlayerReply, Request}
import com.typed.player.player_interface.protocol.PlayerInterfaceReplies.{Error, Ok}
import com.typed.player.behavior.PlayerBehaviorFactory
import com.typed.player.behavior.protocol.PlayerCommands.PlayerCommand
import com.typed.player.behavior.protocol.PlayerReplies.Reply
import com.typed.player.player_interface.PlayerInterface.StashSize

object PlayerInterface {

  val StashSize = 20

  def apply(): Behavior[Command] = Behaviors.setup { ctx =>
    val player = ctx.spawn(PlayerBehaviorFactory.initial(), "player")
    PlayerInterface(Translators.Stopped, player).behavior
  }

  private def apply[C <: PlayerCommand](
      translator: Translator[C],
      player: ActorRef[C]): PlayerInterface[C] = new PlayerInterface[C](translator, player)
}

private class PlayerInterface[C <: PlayerCommand](translator: Translator[C], player: ActorRef[C]) {

  val behavior: Behavior[Command] = Behaviors.receive {
    case (ctx, request: Request) if translator.isDefinedAt(request) =>
      val playerCommand = translateToPlayerCommand(ctx, request)
      player ! playerCommand
      waitForPlayerReply()

    case (_, request) =>
      request.replyTo ! Error(s"Cannot process ${request.toString} in ${this.toString}")
      Behaviors.same
  }

  def translateToPlayerCommand(ctx: ActorContext[Command], request: Request): C = {
    val adapter = ctx.messageAdapter(PlayerReply(_, request.replyTo))
    translator(request)(adapter)
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  def waitForPlayerReply(): Behavior[Command] = {
    Behaviors.withStash(StashSize) { stash =>
      Behaviors.receiveMessage {
        case cmd: Request =>
          stash.stash(cmd)
          Behaviors.same

        case playerReply: PlayerReply =>
          playerReply.replyTo ! Ok(playerReply.reply.state)
          val next = nextState(playerReply.reply)
          stash.unstashAll(next.behavior)
      }
    }
  }

  def nextState(playerReply: Reply): PlayerInterface[_] = {
    import com.typed.player.behavior.protocol.PlayerReplies._
    playerReply match {
      case PlayToggled(_) => this
      case ShuffleToggled(_) => this
      case SkippedFromFirst(_, replyTo) => PlayerInterface(Translators.MiddleTrack, replyTo)
      case Skipped(_) => this
      case SkippedToLastTrack(_, replyTo) => PlayerInterface(Translators.LastTrack, replyTo)
      case SkippedBackFromLastTrack(_, replyTo) => PlayerInterface(Translators.MiddleTrack, replyTo)
      case SkippedBack(_) => this
      case SkippedBackToFirst(_, replyTo) => PlayerInterface(Translators.FirstTrack, replyTo)
      case FirstTrackEnqueued(_, replyTo) => PlayerInterface(Translators.OnlyTrack, replyTo)
      case SecondTrackEnqueued(_, replyTo) => PlayerInterface(Translators.FirstTrack, replyTo)
      case TrackEnqueuedAfterLast(_, replyTo) => PlayerInterface(Translators.MiddleTrack, replyTo)
      case TrackEnqueued(_) => this
      case Stopped(_, replyTo) => PlayerInterface(Translators.Stopped, replyTo)
    }
  }
}
