package com.typed.player.behavior

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.models.{Player, Track}
import com.typed.player.behavior.protocol.PlayerCommands.{EnqueueFirstTrack, ToggleShuffle}

class StoppedBehaviorSpec extends BaseSpec {

  trait Context {
    val testTrack = Track("test")
    val initialState = Player.empty
    val stoppedBehavior = BehaviorTestKit(PlayerBehaviorFactory.initial())
  }

  "Stopped behavior" should "enqueue the first track" in new Context {
    val enqueue = EnqueueFirstTrack(testTrack, playerProbe.ref)

    stoppedBehavior.run(enqueue)

    val reply = playerProbe.receiveMessage()
    reply.state shouldBe initialState.enqueue(testTrack)
  }

  it should "toggle shuffle" in new Context {
    val toggleShuffle = ToggleShuffle(playerProbe.ref)

    stoppedBehavior.run(toggleShuffle)

    val reply = playerProbe.receiveMessage()
    reply.state shouldBe initialState.toggleShuffle()
  }
}
