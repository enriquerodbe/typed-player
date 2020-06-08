package com.typed.player.behavior

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.behavior.protocol.PlayerCommands._
import com.typed.player.behavior.protocol.PlayerReplies.{SkippedBackFromLastTrack, Stopped, TrackEnqueuedAfterLast}
import com.typed.player.models.{Player, Track}

class LastTrackBehaviorSpec extends BaseSpec {

  trait Context {
    val initialState =
      Player.empty
        .enqueue(Track("1"))
        .enqueue(Track("2"))
        .enqueue(Track("3"))
        .skip()
        .skip()

    val lastTrackBehavior = BehaviorTestKit(PlayerBehaviorFactory.lastTrack(initialState))
  }

  "Last track behavior" should "toggle play" in new Context {
    lastTrackBehavior.run(TogglePlay(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.togglePlay()
  }

  it should "toggle shuffle" in new Context {
    lastTrackBehavior.run(ToggleShuffle(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.toggleShuffle()
  }

  it should "skip back" in new Context {
    lastTrackBehavior.run(SkipBack(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.skipBack()
    reply shouldBe a [SkippedBackFromLastTrack]
  }

  it should "enqueue track" in new Context {
    val track = Track("Test track")
    lastTrackBehavior.run(EnqueueTrack(track, playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe initialState.enqueue(track)
    reply shouldBe a [TrackEnqueuedAfterLast]
  }

  it should "stop" in new Context {
    lastTrackBehavior.run(Stop(playerProbe.ref))

    val reply = playerProbe.receiveMessage()

    reply.state shouldBe Player.empty
    reply shouldBe a [Stopped]
  }
}
