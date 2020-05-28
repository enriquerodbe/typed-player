package com.typed.player.queue

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import com.typed.player.models.Queue
import com.typed.player.queue.QueueCommands._
import com.typed.player.queue.QueueReplies._

private case class StoppedBehavior(state: Queue, ctx: ActorContext[QueueCommand])
  extends QueueBehavior[StoppedCommand] {

  override def receiveMessage(cmd: StoppedCommand): Behavior[QueueCommand] = cmd match {
    case ToggleShuffle(replyTo) =>
      val newState = state.toggleShuffle()
      replyTo ! ShuffleToggled(newState)
      QueueBehaviorFactory.stopped(newState)

    case EnqueueFirstTrack(track, replyTo) =>
      val newState = state.enqueue(track)
      replyTo ! FirstTrackEnqueued(newState, ctx.self)
      QueueBehaviorFactory.playingOnlyTrack(newState)
  }
}
