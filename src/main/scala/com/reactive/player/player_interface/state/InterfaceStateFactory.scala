package com.reactive.player.player_interface.state

import com.reactive.player.queue.QueueCommands.{QueueCommand, StoppedCommand}
import com.reactive.player.queue.QueueReplies._

object InterfaceStateFactory {

  def initial(): InterfaceState[StoppedCommand] = StoppedState

  def fromQueueReply(queueReply: Reply): Option[InterfaceState[_ <: QueueCommand]] = {
    queueReply match {
      case PlayToggled(_) => None
      case ShuffleToggled(_) => None
      case SkippedFromFirst(nextBehavior, _) => Some(PlayingState(nextBehavior))
      case Skipped(_) => None
      case SkippedToLastTrack(nextBehavior, _) => Some(LastTrackState(nextBehavior))
      case SkippedBackFromLastTrack(nextBehavior, _) => Some(PlayingState(nextBehavior))
      case SkippedBack(_) => None
      case SkippedBackToFirst(nextBehavior, _) => Some(FirstTrackState(nextBehavior))
      case FirstTrackEnqueued(nextBehavior, _) => Some(OnlyTrackState(nextBehavior))
      case SecondTrackEnqueued(nextBehavior, _) => Some(FirstTrackState(nextBehavior))
      case TrackEnqueuedAfterLast(nextBehavior, _) => Some(PlayingState(nextBehavior))
      case TrackEnqueued(_) => None
      case Stopped(_) => Some(StoppedState)
    }
  }
}
