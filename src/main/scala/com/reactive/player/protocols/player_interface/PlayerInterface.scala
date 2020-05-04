package com.reactive.player.protocols.player_interface

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.reactive.player.protocols.player_interface.PlayerInterfaceCommands._
import com.reactive.player.protocols.player_interface.PlayerInterfaceReplies._
import com.reactive.player.protocols.queue.{QueueCommands, QueueReplies, StoppedQueue}

object PlayerInterface {

  type Translator[Command] = PartialFunction[Request, ActorRef[QueueReplies.Reply] => Command]

  def apply(): Behavior[Command] = behave(StoppedQueue(), stopped)

  private def behave[T](behavior: Behavior[T], translator: Translator[T]): Behavior[Command] = {
    Behaviors.setup { ctx =>
      val queue = ctx.spawnAnonymous(behavior)

      Behaviors.receiveMessage {
        case request: Request if translator.isDefinedAt(request) =>
          val adapter = request.createAdapter(ctx)
          val queueCommand = translator.apply(request)(adapter)
          queue ! queueCommand
          Behaviors.same

        case request: Request =>
          request.replyTo ! Error
          Behaviors.same

        case reply: QueueReply =>
          reactToReply(reply)
      }
    }
  }

  private val stopped: Translator[QueueCommands.StoppedCommand] = {
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueFirstTrack(track, _)
  }

  private val playingOnlyTrack: Translator[QueueCommands.PlayingOnlyTrackCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case EnqueueTrack(track, _) => QueueCommands.EnqueueSecondTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  private val playingFirstTrack: Translator[QueueCommands.PlayingFirstTrackCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case Skip(_) => QueueCommands.Skip
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  private val playing: Translator[QueueCommands.PlayingCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case Skip(_) => QueueCommands.Skip
    case SkipBack(_) => QueueCommands.SkipBack
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  private val playingLastTrack: Translator[QueueCommands.PlayingLastTrackCommand] = {
    case TogglePlay(_) => QueueCommands.TogglePlay
    case ToggleShuffle(_) => QueueCommands.ToggleShuffle
    case SkipBack(_) => QueueCommands.SkipBack
    case EnqueueTrack(track, _) => QueueCommands.EnqueueTrack(track, _)
    case Stop(_) => QueueCommands.Stop
  }

  private def reactToReply(queueReply: QueueReply): Behavior[Command] = {
    queueReply.replyTo ! Ok

    import com.reactive.player.protocols.queue.QueueReplies._
    queueReply.reply match {
      case PlayToggled(status) =>
        Behaviors.same

      case ShuffleToggled(status) =>
        Behaviors.same

      case Skipped(nextBehavior) =>
        behave(nextBehavior, playing)

      case Skipped =>
        Behaviors.same

      case SkippedToLastTrack(nextBehavior) =>
        behave(nextBehavior, playingLastTrack)

      case SkippedBack(nextBehavior) =>
        behave(nextBehavior, playing)

      case SkippedBack =>
        Behaviors.same

      case SkippedToFirstTrack(nextBehavior) =>
        behave(nextBehavior, playingFirstTrack)

      case FirstTrackEnqueued(nextBehavior) =>
        behave(nextBehavior, playingOnlyTrack)

      case SecondTrackEnqueued(nextBehavior) =>
        behave(nextBehavior, playingFirstTrack)

      case TrackEnqueued(nextBehavior) =>
        behave(nextBehavior, playing)

      case TrackEnqueued =>
        Behaviors.same

      case Stopped =>
        PlayerInterface()
    }
  }
}
