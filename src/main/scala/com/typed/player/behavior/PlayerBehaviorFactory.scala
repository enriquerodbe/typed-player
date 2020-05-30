package com.typed.player.behavior

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Player
import com.typed.player.behavior.protocol.PlayerCommands._

object PlayerBehaviorFactory {

  def initial(): Behavior[StoppedCommand] = stopped(Player.empty).narrow

  private[behavior] def stopped(state: Player): Behavior[PlayerCommand] = Behaviors.receivePartial {
    case (ctx, cmd: StoppedCommand) => StoppedBehavior(state, ctx).receiveMessage(cmd)
  }

  private[behavior] def onlyTrack(state: Player): Behavior[PlayerCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: OnlyTrackCommand) =>
        OnlyTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }

  private[behavior] def firstTrack(state: Player): Behavior[PlayerCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: FirstTrackCommand) =>
        FirstTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }

  private[behavior] def middleTrack(state: Player): Behavior[PlayerCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: MiddleTrackCommand) => MiddleTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }

  private[behavior] def lastTrack(state: Player): Behavior[PlayerCommand] = {
    Behaviors.receivePartial {
      case (ctx, cmd: LastTrackCommand) =>
        LastTrackBehavior(state, ctx).receiveMessage(cmd)
    }
  }
}
