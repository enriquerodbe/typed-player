package com.typed.player.behavior

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.behavior.protocol.PlayerReplies.{SkippedFromFirst, Stopped}
import com.typed.player.models.{Player, Track}

class FirstTrackBehaviorSpec extends BaseSpec {

  trait Context {
    val initialState =
      Player.empty
        .enqueue(Track("1"))
        .enqueue(Track("2"))
        .enqueue(Track("3"))

    val firstTrackBehavior = BehaviorTestKit(PlayerBehaviorFactory.firstTrack(initialState))
  }

  "First track behavior" should "toggle play" in new Context {
    firstTrackBehavior.run(TogglePlay(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.togglePlay()
  }

  it should "toggle shuffle" in new Context {
    firstTrackBehavior.run(ToggleShuffle(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.toggleShuffle()
  }

  it should "skip track" in new Context {
    firstTrackBehavior.run(Skip(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.skip()
    reply shouldBe a [SkippedFromFirst]
  }

  it should "stop" in new Context {
    firstTrackBehavior.run(Stop(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe Player.empty
    reply shouldBe a [Stopped]
  }
}
