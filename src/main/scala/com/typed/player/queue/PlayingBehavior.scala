package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

private case class PlayingBehavior(state: Queue, ctx: ActorContext[QueueCommand])
  extends QueueBehavior[PlayingCommand] {

  override def receiveMessage(cmd: PlayingCommand): Behavior[QueueCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      QueueBehaviorFactory.playing(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      QueueBehaviorFactory.playing(newState)

    case Skip(replyTo) =>
      val newState = state.skip()
      if (newState.isLastTrack) {
        replyTo ! SkippedToLastTrack(newState, ctx.self)
        QueueBehaviorFactory.playingLastTrack(newState)
      } else {
        replyTo ! Skipped(newState)
        QueueBehaviorFactory.playing(newState)
      }

    case SkipBack(replyTo) =>
      val newState = state.skipBack()
      if (newState.isFirstTrack) {
        replyTo ! SkippedBackToFirst(newState, ctx.self)
        QueueBehaviorFactory.playingFirstTrack(newState)
      } else {
        replyTo ! SkippedBack(newState)
        QueueBehaviorFactory.playing(newState)
      }

    case EnqueueTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! TrackEnqueued(newState)
      QueueBehaviorFactory.playing(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state, ctx.self)
      QueueBehaviorFactory.stopped(state)
  }
}
