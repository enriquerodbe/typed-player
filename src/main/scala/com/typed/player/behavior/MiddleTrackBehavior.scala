package com.typed.player.behavior

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Player
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.behavior.protocol.PlayerReplies._

private case class MiddleTrackBehavior(state: Player, ctx: ActorContext[PlayerCommand])
  extends PlayerBehavior[MiddleTrackCommand] {

  override def receiveMessage(cmd: MiddleTrackCommand): Behavior[PlayerCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayerBehaviorFactory.middleTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayerBehaviorFactory.middleTrack(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        replyTo ! SkippedToLastTrack(newState, ctx.self)
        PlayerBehaviorFactory.lastTrack(newState)
      } else {
        replyTo ! Skipped(newState)
        PlayerBehaviorFactory.middleTrack(newState)
      }

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      if (newState.isFirstTrack) {
        replyTo ! SkippedBackToFirst(newState, ctx.self)
        PlayerBehaviorFactory.firstTrack(newState)
      } else {
        replyTo ! SkippedBack(newState)
        PlayerBehaviorFactory.middleTrack(newState)
      }

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      PlayerBehaviorFactory.middleTrack(newState)

    case Stop(replyTo) =>
      val newState = state.stop()
      replyTo ! Stopped(newState, ctx.self)
      PlayerBehaviorFactory.empty(newState)
  }
}
