package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingQueue {

  def apply(state: Queue): Behavior[QueueCommand] = Behaviors.receive {
    case (_, TogglePlay(replyTo)) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingQueue(newState)

    case (_, ToggleShuffle(replyTo)) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingQueue(newState)

    case (ctx, Skip(replyTo)) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        replyTo ! SkippedToLastTrack(newState, ctx.self)
        PlayingLastTrackQueue(newState)
      } else {
        replyTo ! Skipped(newState)
        PlayingQueue(newState)
      }

    case (ctx, SkipBack(replyTo)) =>
      val newState = state.skipBack()
      if (newState.isFirstTrack) {
        replyTo ! SkippedBackToFirst(newState, ctx.self)
        PlayingFirstTrackQueue(newState)
      } else {
        replyTo ! SkippedBack(newState)
        PlayingQueue(newState)
      }

    case (_, EnqueueTrack(track, replyTo)) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      PlayingQueue(newState)

    case (ctx, Stop(replyTo)) =>
      replyTo ! Stopped(state, ctx.self)
      StoppedQueue(state)

    case _ =>
      Behaviors.unhandled
  }
}
