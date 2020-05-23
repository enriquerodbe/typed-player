package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingFirstTrackQueue {

  def apply(state: Queue): Behavior[QueueCommand] = Behaviors.receive {
    case (_, TogglePlay(replyTo)) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingFirstTrackQueue(newState)

    case (_, ToggleShuffle(replyTo)) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingFirstTrackQueue(newState)

    case (ctx, Skip(replyTo)) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        replyTo ! SkippedToLastTrack(newState, ctx.self)
        PlayingLastTrackQueue(newState)
      } else {
        replyTo ! SkippedFromFirst(newState, ctx.self)
        PlayingQueue(newState)
      }

    case (_, EnqueueTrack(track, replyTo)) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      PlayingFirstTrackQueue(newState)

    case (ctx, Stop(replyTo)) =>
      replyTo ! Stopped(state, ctx.self)
      StoppedQueue(state)

    case _ =>
      Behaviors.unhandled
  }
}
