package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingQueue {

  def apply(state: Queue): Behavior[PlayingCommand] = Behaviors.receiveMessage {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingQueue(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingQueue(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        val nextBehavior = PlayingLastTrackQueue(newState)
        replyTo ! SkippedToLastTrack(nextBehavior, newState)
        Behaviors.stopped
      } else {
        replyTo ! Skipped(newState)
        PlayingQueue(newState)
      }

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      if (newState.isFirstTrack) {
        val nextBehavior = PlayingFirstTrackQueue(newState)
        replyTo ! SkippedBackToFirst(nextBehavior, newState)
        Behaviors.stopped
      } else {
        replyTo ! SkippedBack(newState)
        PlayingQueue(newState)
      }

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      PlayingQueue(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state)
      Behaviors.stopped
  }
}
