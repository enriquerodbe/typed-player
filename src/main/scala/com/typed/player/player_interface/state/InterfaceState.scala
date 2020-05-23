package com.typed.player.player_interface.state

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.typed.player.player_interface.PlayerInterfaceCommands.{Command, QueueReply, Request}
import com.typed.player.player_interface.PlayerInterfaceReplies.{Error, Ok}
import com.typed.player.queue.QueueCommands.QueueCommand
import com.typed.player.queue.QueueReplies
import com.typed.player.queue.QueueReplies.Reply

trait InterfaceState[QC <: QueueCommand] {

  type QueueCommandConstructor = ActorRef[QueueReplies.Reply] => QC
  type Translator = PartialFunction[Request, QueueCommandConstructor]

  val queue: ActorRef[QC]
  val translator: Translator

  val behavior: Behavior[Command] = Behaviors.receive {
    case (ctx, request: Request) if translator.isDefinedAt(request) =>
      val queueCommand = translateToQueueCommand(ctx, request)
      queue ! queueCommand
      waitForQueueReply()

    case (_, request) =>
      request.replyTo ! Error(s"$request in $this")
      Behaviors.same
  }

  private def translateToQueueCommand(ctx: ActorContext[Command], request: Request): QC = {
    val adapter = ctx.messageAdapter(QueueReply(_, request.replyTo))
    translator(request)(adapter)
  }

  private def waitForQueueReply(): Behavior[Command] = {
    Behaviors.withStash(100) { stash =>
      Behaviors.receiveMessage {
        case cmd: Request =>
          stash.stash(cmd)
          Behaviors.same

        case queueReply: QueueReply =>
          queueReply.replyTo ! Ok(queueReply.reply.state)
          val nextBehavior = nextState(queueReply.reply).behavior
          stash.unstashAll(nextBehavior)
      }
    }
  }

  private def nextState(queueReply: Reply): InterfaceState[_ <: QueueCommand] = {
    import com.typed.player.queue.QueueReplies._
    queueReply match {
      case PlayToggled(_) => this
      case ShuffleToggled(_) => this
      case SkippedFromFirst(_, replyTo) => PlayingState(replyTo)
      case Skipped(_) => this
      case SkippedToLastTrack(_, replyTo) => LastTrackState(replyTo)
      case SkippedBackFromLastTrack(_, replyTo) => PlayingState(replyTo)
      case SkippedBack(_) => this
      case SkippedBackToFirst(_, replyTo) => FirstTrackState(replyTo)
      case FirstTrackEnqueued(_, replyTo) => OnlyTrackState(replyTo)
      case SecondTrackEnqueued(_, replyTo) => FirstTrackState(replyTo)
      case TrackEnqueuedAfterLast(_, replyTo) => PlayingState(replyTo)
      case TrackEnqueued(_) => this
      case Stopped(_, replyTo) => StoppedState(replyTo)
    }
  }
}
