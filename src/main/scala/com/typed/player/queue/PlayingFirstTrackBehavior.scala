package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

private case class PlayingFirstTrackBehavior(state: Queue, ctx: ActorContext[QueueCommand])
  extends QueueBehavior[PlayingFirstTrackCommand] {

  override def receiveMessage(cmd: PlayingFirstTrackCommand): Behavior[QueueCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      QueueBehaviorFactory.playingFirstTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      QueueBehaviorFactory.playingFirstTrack(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        replyTo ! SkippedToLastTrack(newState, ctx.self)
        QueueBehaviorFactory.playingLastTrack(newState)
      } else {
        replyTo ! SkippedFromFirst(newState, ctx.self)
        QueueBehaviorFactory.playing(newState)
      }

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      QueueBehaviorFactory.playingFirstTrack(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state, ctx.self)
      QueueBehaviorFactory.stopped(state)
  }
}
