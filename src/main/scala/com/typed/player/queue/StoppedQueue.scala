package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object StoppedQueue {

  def apply(state: Queue = Queue.empty): Behavior[QueueCommand] = Behaviors.receive {
    case (_, ToggleShuffle(replyTo)) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      StoppedQueue(newState)

    case (ctx, EnqueueFirstTrack(track, replyTo)) =>
      val newState = state.enqueue(track)
      replyTo ! FirstTrackEnqueued(newState, ctx.self)
      PlayingOnlyTrackQueue(newState)

    case _ =>
      Behaviors.unhandled
  }
}
