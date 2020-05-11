package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object StoppedQueue {

  def apply(state: Queue = Queue.empty): Behavior[StoppedCommand] = Behaviors.receiveMessage {
    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      StoppedQueue(newState)

    case EnqueueFirstTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      val nextBehavior = PlayingOnlyTrackQueue(newState)
      replyTo ! FirstTrackEnqueued(nextBehavior, newState)
      Behaviors.stopped
  }
}
