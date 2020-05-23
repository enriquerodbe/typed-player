package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingLastTrackQueue {

  def apply(state: Queue): Behavior[QueueCommand] = Behaviors.receive {
    case (_, TogglePlay(replyTo)) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingLastTrackQueue(newState)

    case (_, ToggleShuffle(replyTo)) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingLastTrackQueue(newState)

    case (ctx, SkipBack(replyTo)) =>
      val newState = state.skipBack()
      replyTo ! SkippedBackFromLastTrack(newState, ctx.self)
      PlayingQueue(newState)

    case (ctx, EnqueueTrack(track, replyTo)) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueuedAfterLast(newState, ctx.self)
      PlayingQueue(newState)

    case (ctx, Stop(replyTo)) =>
      replyTo ! Stopped(state, ctx.self)
      StoppedQueue(state)

    case _ =>
      Behaviors.unhandled
  }
}
