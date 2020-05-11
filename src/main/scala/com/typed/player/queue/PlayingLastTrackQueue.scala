package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

object PlayingLastTrackQueue {

  def apply(state: Queue): Behavior[PlayingLastTrackCommand] = Behaviors.receiveMessage {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      PlayingLastTrackQueue(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      PlayingLastTrackQueue(newState)

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      val nextBehavior = PlayingQueue(newState)
      replyTo ! SkippedBackFromLastTrack(nextBehavior, newState)
      Behaviors.stopped

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      val nextBehavior = PlayingQueue(newState)
      replyTo ! TrackEnqueuedAfterLast(nextBehavior, newState)
      Behaviors.stopped

    case Stop(replyTo) =>
      replyTo ! Stopped(state)
      Behaviors.stopped
  }
}
