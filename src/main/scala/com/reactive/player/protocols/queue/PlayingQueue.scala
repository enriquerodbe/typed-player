package com.reactive.player.protocols.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.reactive.player.models.Queue
import com.reactive.player.protocols.queue.QueueCommands._
import com.reactive.player.protocols.queue.QueueReplies._

object PlayingQueue {

  def apply(state: Queue): Behavior[PlayingCommand] = {
    Behaviors.receiveMessage {
      case TogglePlay(replyTo) =>
        val newState = state.togglePlay()
        replyTo ! PlayToggled(state.playing)
        PlayingQueue(newState)

      case ToggleShuffle(replyTo) =>
        val newState = state.toggleShuffle()
        replyTo ! ShuffleToggled(newState.shuffleMode)
        PlayingQueue(newState)

      case Skip(replyTo) =>
        val newState = state.skip()
        if (newState.isLastTrack) {
          val nextBehavior = PlayingLastTrackQueue(newState)
          replyTo ! SkippedToLastTrack(nextBehavior)
          Behaviors.stopped
        } else {
          replyTo ! Skipped
          PlayingQueue(newState)
        }

      case SkipBack(replyTo) =>
        val newState = state.skipBack()
        if (newState.isFirstTrack) {
          val nextBehavior = PlayingFirstTrackQueue(newState)
          replyTo ! SkippedToFirstTrack(nextBehavior)
          Behaviors.stopped
        } else {
          replyTo ! SkippedBack
          PlayingQueue(newState)
        }

      case EnqueueTrack(track, replyTo) =>
        val newState = state.enqueue(track)
        replyTo ! TrackEnqueued
        PlayingQueue(newState)

      case Stop(replyTo) =>
        replyTo ! Stopped
        Behaviors.stopped
    }
  }
}
