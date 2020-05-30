package com.typed.player.behavior

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Player
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.behavior.protocol.PlayerReplies._

private case class FirstTrackBehavior(state: Player, ctx: ActorContext[PlayerCommand])
  extends PlayerBehavior[FirstTrackCommand] {

  override def receiveMessage(cmd: FirstTrackCommand): Behavior[PlayerCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayerBehaviorFactory.firstTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayerBehaviorFactory.firstTrack(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        replyTo ! SkippedToLastTrack(newState, ctx.self)
        PlayerBehaviorFactory.lastTrack(newState)
      } else {
        replyTo ! SkippedFromFirst(newState, ctx.self)
        PlayerBehaviorFactory.middleTrack(newState)
      }

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      PlayerBehaviorFactory.firstTrack(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state, ctx.self)
      PlayerBehaviorFactory.stopped(state)
  }
}
