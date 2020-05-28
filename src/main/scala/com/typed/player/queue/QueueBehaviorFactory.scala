package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._

object QueueBehaviorFactory {

  def initial(): Behavior[StoppedCommand] = stopped(Queue.empty).narrow

  private[queue] def stopped(state: Queue): Behavior[QueueCommand] = Behaviors.receivePartial {
    case (ctx, cmd: StoppedCommand) => StoppedBehavior(state, ctx).receiveMessage(cmd)
  }

  private[queue] def playingOnlyTrack(state: Queue): Behavior[QueueCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: PlayingOnlyTrackCommand) =>
        PlayingOnlyTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }

  private[queue] def playingFirstTrack(state: Queue): Behavior[QueueCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: PlayingFirstTrackCommand) =>
        PlayingFirstTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }

  private[queue] def playing(state: Queue): Behavior[QueueCommand] = Behaviors.receivePartial {
    case (ctx, cmd: PlayingCommand) => PlayingBehavior(state, ctx).receiveMessage(cmd)
  }

  private[queue] def playingLastTrack(state: Queue): Behavior[QueueCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: PlayingLastTrackCommand) =>
        PlayingLastTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }
}
