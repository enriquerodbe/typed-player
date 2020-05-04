package com.reactive.player.protocols.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.reactive.player.models.Queue
import com.reactive.player.protocols.queue.QueueCommands._
import com.reactive.player.protocols.queue.QueueReplies._

object PlayingLastTrackQueue {

  def apply(state: Queue): Behavior[PlayingLastTrackCommand] = Behaviors.receiveMessage {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(state.playing)
      PlayingLastTrackQueue(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState.shuffleMode)
      PlayingLastTrackQueue(newState)

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      val nextBehavior = PlayingQueue(newState)
      replyTo ! SkippedBack(nextBehavior)
      Behaviors.stopped

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      val nextBehavior = PlayingQueue(newState)
      replyTo ! TrackEnqueued(nextBehavior)
      Behaviors.stopped

    case Stop(replyTo) =>
      replyTo ! Stopped
      Behaviors.stopped
  }
}
