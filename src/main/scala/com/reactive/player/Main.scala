package com.reactive.player

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import com.reactive.player.models.Track
import com.reactive.player.player_interface.PlayerInterface
import com.reactive.player.player_interface.PlayerInterfaceCommands._
import com.reactive.player.player_interface.PlayerInterfaceReplies._
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, Future}

object Main extends App {

  implicit val timeout: Timeout = 3.seconds
  implicit val system: ActorSystem[Command] = ActorSystem(PlayerInterface(), "reactive-player")
  var command = ""
  var trackName = 1

  try {
    do {
      command = io.StdIn.readLine()
      sendCommand(command)
    } while (command != "q")
  } finally {
    println("terminating...")
    system.terminate()
  }

  private def sendCommand(commandString: String): Unit = {
    val futureResponse = commandString match {
      case "a" => system.ask[Reply](TogglePlay)
      case "h" => system.ask[Reply](ToggleShuffle)
      case "n" => system.ask[Reply](Skip)
      case "p" => system.ask[Reply](SkipBack)
      case "e" => system.ask[Reply](EnqueueTrack(makeTrack(), _))
      case "t" | "q" => system.ask[Reply](Stop)
      case cmd => Future.successful(Error(s"Invalid command '$cmd'"))
    }

    val result = Await.result(futureResponse, timeout.duration)

    result match {
      case Ok(queue) => println(queue)
      case Error(message) => println(s"Error: $message")
    }
  }

  private def makeTrack(): Track = {
    val track = Track(trackName.toString)
    trackName += 1
    track
  }
}
