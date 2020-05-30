package com.typed.player.behavior

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Player
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.behavior.protocol.PlayerReplies._

private case class OnlyTrackBehavior(state: Player, ctx: ActorContext[PlayerCommand])
  extends PlayerBehavior[OnlyTrackCommand] {

  override def receiveMessage(cmd: OnlyTrackCommand): Behavior[PlayerCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayerBehaviorFactory.onlyTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayerBehaviorFactory.onlyTrack(newState)

    case EnqueueSecondTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! SecondTrackEnqueued(newState, ctx.self)
      PlayerBehaviorFactory.firstTrack(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state, ctx.self)
      PlayerBehaviorFactory.stopped(state)
  }
}
