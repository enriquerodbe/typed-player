package com.typed.player.behavior

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.models.{Player, Track}
import com.typed.player.behavior.protocol.PlayerCommands.{Skip, SkipBack, Stop, TogglePlay, ToggleShuffle}
import com.typed.player.behavior.protocol.PlayerReplies.{Skipped, SkippedBack, SkippedBackToFirst, SkippedToLastTrack, Stopped}

class MiddleTrackBehaviorSpec extends BaseSpec {

  trait Context {
    val initialState =
      Player.empty
        .enqueue(Track("1"))
        .enqueue(Track("2"))
        .enqueue(Track("3"))
        .enqueue(Track("4"))
        .enqueue(Track("5"))
        .skip()
        .skip()

    val middleTrackBehavior = BehaviorTestKit(PlayerBehaviorFactory.middleTrack(initialState))
  }

  "Middle track behavior" should "toggle play" in new Context {
    middleTrackBehavior.run(TogglePlay(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.togglePlay()
  }

  it should "toggle shuffle" in new Context {
    middleTrackBehavior.run(ToggleShuffle(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.toggleShuffle()
  }

  it should "skip back" in new Context {
    middleTrackBehavior.run(SkipBack(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.skipBack()
    reply shouldBe a [SkippedBack]
  }

  it should "skip back to first track" in new Context {
    middleTrackBehavior.run(SkipBack(playerProbe.ref))
    playerProbe.receiveMessage() // ignore response
    middleTrackBehavior.run(SkipBack(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.skipBack().skipBack()
    reply shouldBe a [SkippedBackToFirst]
  }

  it should "skip track" in new Context {
    middleTrackBehavior.run(Skip(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.skip()
    reply shouldBe a [Skipped]
  }

  it should "skip to last track" in new Context {
    middleTrackBehavior.run(Skip(playerProbe.ref))
    playerProbe.receiveMessage() // ignore response
    middleTrackBehavior.run(Skip(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.skip().skip()
    reply shouldBe a [SkippedToLastTrack]
  }

  it should "stop" in new Context {
    middleTrackBehavior.run(Stop(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe Player.empty
    reply shouldBe a [Stopped]
  }
}
