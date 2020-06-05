package com.typed.player.behavior

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Player
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.behavior.protocol.PlayerReplies._

private case class LastTrackBehavior(state: Player, ctx: ActorContext[PlayerCommand])
  extends PlayerBehavior[LastTrackCommand] {

  override def receiveMessage(cmd: LastTrackCommand): Behavior[PlayerCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayerBehaviorFactory.lastTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayerBehaviorFactory.lastTrack(newState)

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      replyTo ! SkippedBackFromLastTrack(newState, ctx.self)
      PlayerBehaviorFactory.middleTrack(newState)

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueuedAfterLast(newState, ctx.self)
      PlayerBehaviorFactory.middleTrack(newState)

    case Stop(replyTo) =>
      val newState = state.stop()
      replyTo ! Stopped(newState, ctx.self)
      PlayerBehaviorFactory.empty(newState)
  }
}
