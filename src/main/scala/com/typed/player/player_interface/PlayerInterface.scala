package com.typed.player.player_interface

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.typed.player.player_interface.PlayerInterfaceCommands._
import com.typed.player.player_interface.PlayerInterfaceReplies._
import com.typed.player.player_interface.state.{InterfaceState, InterfaceStateFactory}
import com.typed.player.queue.QueueCommands.QueueCommand
import com.typed.player.queue.QueueReplies
import com.typed.player.player_interface.PlayerInterfaceCommands.{Command, QueueReply, Request}
import com.typed.player.player_interface.state.InterfaceState
import com.typed.player.queue.QueueCommands.QueueCommand
import com.typed.player.queue.QueueReplies.Reply

object PlayerInterface {

  def apply(): Behavior[Command] = Behaviors.setup { implicit ctx =>
    val initialState = InterfaceStateFactory.initial()
    new PlayerInterface(initialState).behavior()
  }
}

class PlayerInterface[QC <: QueueCommand] private (
    state: InterfaceState[QC])(
    implicit ctx: ActorContext[Command]) {

  def behavior(): Behavior[Command] = behave(None)

  private def behave(maybeQueue: Option[ActorRef[QC]]): Behavior[Command] = {
    val queue = maybeQueue.getOrElse(ctx.spawnAnonymous(state.queueBehavior))

    Behaviors.receiveMessage {
      case request: Request if state.translator.isDefinedAt(request) =>
        val queueCommand = translateToQueueCommand(request)
        queue ! queueCommand
        waitForQueueReply(queue)

      case request =>
        request.replyTo ! Error(s"$request in $state")
        Behaviors.same
    }
  }

  private def translateToQueueCommand(request: Request): QC = {
    val adapter = ctx.messageAdapter(QueueReply(_, request.replyTo))
    state.translator(request)(adapter)
  }

  private def waitForQueueReply(queue: ActorRef[QC]): Behavior[Command] = {
    Behaviors.withStash(100) { stash =>
      Behaviors.receiveMessage {
        case cmd: Request =>
          stash.stash(cmd)
          Behaviors.same

        case queueReply: QueueReply =>
          queueReply.replyTo ! Ok(queueReply.reply.state)
          val nextBehavior = getNextBehavior(queue, queueReply.reply)
          stash.unstashAll(nextBehavior)
      }
    }
  }

  private def getNextBehavior(queue: ActorRef[QC], reply: Reply): Behavior[Command] = {
    val maybeNewState = InterfaceStateFactory.fromQueueReply(reply)
    maybeNewState match {
      case Some(newState) => new PlayerInterface(newState).behave(None)
      case None => this.behave(Some(queue))
    }
  }
}
