package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

private case class PlayingLastTrackBehavior(state: Queue, ctx: ActorContext[QueueCommand])
  extends QueueBehavior[PlayingLastTrackCommand] {

  override def receiveMessage(cmd: PlayingLastTrackCommand): Behavior[QueueCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      QueueBehaviorFactory.playingLastTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      QueueBehaviorFactory.playingLastTrack(newState)

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      replyTo ! SkippedBackFromLastTrack(newState, ctx.self)
      QueueBehaviorFactory.playing(newState)

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueuedAfterLast(newState, ctx.self)
      QueueBehaviorFactory.playing(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state, ctx.self)
      QueueBehaviorFactory.stopped(state)
  }
}
