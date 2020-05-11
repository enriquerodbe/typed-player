package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingFirstTrackQueue {

  def apply(state: Queue): Behavior[PlayingFirstTrackCommand] = Behaviors.receiveMessage {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingFirstTrackQueue(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingFirstTrackQueue(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        val nextBehavior = PlayingLastTrackQueue(newState)
        replyTo ! SkippedToLastTrack(nextBehavior, newState)
      } else {
        val nextBehavior = PlayingQueue(newState)
        replyTo ! SkippedFromFirst(nextBehavior, newState)
      }
      Behaviors.stopped

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      PlayingFirstTrackQueue(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state)
      Behaviors.stopped
  }
}
