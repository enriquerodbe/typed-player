package com.reactive.player.protocols.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.reactive.player.models.Queue
import com.reactive.player.protocols.queue.QueueCommands._
import com.reactive.player.protocols.queue.QueueReplies._

object PlayingFirstTrackQueue {

  def apply(state: Queue): Behavior[PlayingFirstTrackCommand] = Behaviors.receiveMessage {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(state.playing)
      PlayingFirstTrackQueue(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState.shuffleMode)
      PlayingFirstTrackQueue(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        val nextBehavior = PlayingLastTrackQueue(newState)
        replyTo ! SkippedToLastTrack(nextBehavior)
      } else {
        val nextBehavior = PlayingQueue(newState)
        replyTo ! Skipped(nextBehavior)
      }
      Behaviors.stopped

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued
      PlayingFirstTrackQueue(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped
      Behaviors.stopped
  }
}
