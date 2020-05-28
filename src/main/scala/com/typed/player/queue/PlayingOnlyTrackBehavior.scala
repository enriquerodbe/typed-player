package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

private case class PlayingOnlyTrackBehavior(state: Queue, ctx: ActorContext[QueueCommand])
  extends QueueBehavior[PlayingOnlyTrackCommand] {

  override def receiveMessage(cmd: PlayingOnlyTrackCommand): Behavior[QueueCommand] = cmd match {
    case TogglePlay(replyTo) =>
      val newState = state.togglePlay()
      replyTo ! PlayToggled(newState)
      QueueBehaviorFactory.playingOnlyTrack(newState)

    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      QueueBehaviorFactory.playingOnlyTrack(newState)

    case EnqueueSecondTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! SecondTrackEnqueued(newState, ctx.self)
      QueueBehaviorFactory.playingFirstTrack(newState)

    case Stop(replyTo) =>
      replyTo ! Stopped(state, ctx.self)
      QueueBehaviorFactory.stopped(state)
  }
}
