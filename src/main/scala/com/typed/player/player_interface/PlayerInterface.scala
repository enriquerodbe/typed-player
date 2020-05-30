package com.typed.player.player_interface

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.typed.player.player_interface.PlayerInterfaceCommands.{Command, QueueReply, Request}
import com.typed.player.player_interface.PlayerInterfaceReplies.{Error, Ok}
import com.typed.player.queue.QueueBehaviorFactory
import com.typed.player.queue.QueueCommands.QueueCommand
import com.typed.player.queue.QueueReplies.Reply

object PlayerInterface {

  def apply(): Behavior[Command] = Behaviors.setup { ctx =>
    val queue = ctx.spawn(QueueBehaviorFactory.initial(), "queue")
    PlayerInterface(Translators.Stopped, queue).behavior
  }

  private def apply[QC <: QueueCommand](
      translator: Translator[QC],
      queue: ActorRef[QC]): PlayerInterface[QC] = new PlayerInterface[QC](translator, queue)
}

private class PlayerInterface[QC <: QueueCommand](translator: Translator[QC], queue: ActorRef[QC]) {

  val behavior: Behavior[Command] = Behaviors.receive {
    case (ctx, request: Request) if translator.isDefinedAt(request) =>
      val queueCommand = translateToQueueCommand(ctx, request)
      queue ! queueCommand
      waitForQueueReply()

    case (_, request) =>
      request.replyTo ! Error(s"Cannot process ${request.toString} in ${this.toString}")
      Behaviors.same
  }

  def translateToQueueCommand(ctx: ActorContext[Command], request: Request): QC = {
    val adapter = ctx.messageAdapter(QueueReply(_, request.replyTo))
    translator(request)(adapter)
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  def waitForQueueReply(): Behavior[Command] = {
    Behaviors.withStash(100) { stash =>
      Behaviors.receiveMessage {
        case cmd: Request =>
          stash.stash(cmd)
          Behaviors.same

        case queueReply: QueueReply =>
          queueReply.replyTo ! Ok(queueReply.reply.state)
          val next = nextState(queueReply.reply)
          stash.unstashAll(next.behavior)
      }
    }
  }

  def nextState(queueReply: Reply): PlayerInterface[_] = {
    import com.typed.player.queue.QueueReplies._
    queueReply match {
      case PlayToggled(_) => this
      case ShuffleToggled(_) => this
      case SkippedFromFirst(_, replyTo) => PlayerInterface(Translators.Playing, replyTo)
      case Skipped(_) => this
      case SkippedToLastTrack(_, replyTo) => PlayerInterface(Translators.LastTrack, replyTo)
      case SkippedBackFromLastTrack(_, replyTo) => PlayerInterface(Translators.Playing, replyTo)
      case SkippedBack(_) => this
      case SkippedBackToFirst(_, replyTo) => PlayerInterface(Translators.FirstTrack, replyTo)
      case FirstTrackEnqueued(_, replyTo) => PlayerInterface(Translators.OnlyTrack, replyTo)
      case SecondTrackEnqueued(_, replyTo) => PlayerInterface(Translators.FirstTrack, replyTo)
      case TrackEnqueuedAfterLast(_, replyTo) => PlayerInterface(Translators.Playing, replyTo)
      case TrackEnqueued(_) => this
      case Stopped(_, replyTo) => PlayerInterface(Translators.Stopped, replyTo)
    }
  }
}
