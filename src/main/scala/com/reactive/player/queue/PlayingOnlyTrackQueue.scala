package com.reactive.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.reactive.player.models.Queue
import com.reactive.player.queue.QueueCommands._
import com.reactive.player.queue.QueueReplies._

object PlayingOnlyTrackQueue {

  def apply(state: Queue): Behavior[PlayingOnlyTrackCommand] = Behaviors.receiveMessage {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingOnlyTrackQueue(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingOnlyTrackQueue(newState)

    case EnqueueSecondTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      val nextBehavior = PlayingFirstTrackQueue(newState)
      replyTo ! SecondTrackEnqueued(nextBehavior, newState)
      Behaviors.stopped

    case Stop(replyTo) =>
      replyTo ! Stopped(state)
      Behaviors.stopped
  }
}
