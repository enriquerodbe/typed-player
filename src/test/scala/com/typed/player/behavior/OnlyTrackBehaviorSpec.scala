package com.typed.player.behavior

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.behavior.protocol.PlayerCommands.{EnqueueSecondTrack, Stop, TogglePlay, ToggleShuffle}
import com.typed.player.behavior.protocol.PlayerReplies.{SecondTrackEnqueued, Stopped}
import com.typed.player.models.{Player, Track}

class OnlyTrackBehaviorSpec extends BaseSpec {

  trait Context {
    val initialState =
      Player.empty.enqueue(Track("1"))

    val onlyTrackBehavior = BehaviorTestKit(PlayerBehaviorFactory.onlyTrack(initialState))
  }

  "Only track behavior" should "toggle play" in new Context {
    onlyTrackBehavior.run(TogglePlay(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.togglePlay()
  }

  it should "toggle shuffle" in new Context {
    onlyTrackBehavior.run(ToggleShuffle(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.toggleShuffle()
  }

  it should "enqueue track" in new Context {
    val track = Track("test")
    onlyTrackBehavior.run(EnqueueSecondTrack(track, playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.enqueue(track)
    reply shouldBe a [SecondTrackEnqueued]
  }

  it should "stop" in new Context {
    onlyTrackBehavior.run(Stop(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe Player.empty
    reply shouldBe a [Stopped]
  }
}
