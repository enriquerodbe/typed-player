package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingOnlyTrackQueue {

  def apply(state: Queue): Behavior[QueueCommand] = Behaviors.receive {
    case (_, TogglePlay(replyTo)) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingOnlyTrackQueue(newState)

    case (_, ToggleShuffle(replyTo)) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingOnlyTrackQueue(newState)

    case (ctx, EnqueueSecondTrack(track, replyTo)) =>
      val newState = state.enqueue(track)
      replyTo ! SecondTrackEnqueued(newState, ctx.self)
      PlayingFirstTrackQueue(newState)

    case (ctx, Stop(replyTo)) =>
      replyTo ! Stopped(state, ctx.self)
      StoppedQueue(state)

    case _ =>
      Behaviors.unhandled
  }
}
