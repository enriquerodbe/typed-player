package com.typed.player

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import com.typed.player.models.Track
import com.typed.player.player_interface.PlayerInterface
import com.typed.player.player_interface.protocol.PlayerInterfaceCommands._
import com.typed.player.player_interface.protocol.PlayerInterfaceReplies._
import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, Future}

@SuppressWarnings(Array("org.wartremover.warts.All"))
object Main extends App {

  implicit val timeout: Timeout = 3.seconds
  implicit val system: ActorSystem[Command] = ActorSystem(PlayerInterface(), "typed-player")
  var command = ""
  var trackName = 0

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
      case Ok(state) => println(state)
      case Error(message) => println(s"Error: $message")
    }
  }

  private def makeTrack(): Track = {
    trackName += 1
    Track(trackName.toString)
  }
}
