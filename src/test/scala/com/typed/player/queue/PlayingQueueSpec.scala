package com.typed.player.queue

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.models.{Queue, Track}
import com.typed.player.queue.QueueCommands.{Skip, SkipBack, Stop, TogglePlay, ToggleShuffle}
import com.typed.player.queue.QueueReplies.{SkippedBackToFirst, SkippedToLastTrack, Stopped}

class PlayingQueueSpec extends BaseSpec {

  trait Context {
    val initialState =
      Queue.empty
        .enqueue(Track("1"))
        .enqueue(Track("2"))
        .enqueue(Track("3"))
        .skip()
        .skip()

    val playingBehavior = BehaviorTestKit(QueueBehaviorFactory.playing(initialState))
  }

  "a playing queue" should "toggle play" in new Context {
    playingBehavior.run(TogglePlay(queueTestProbe.ref))

    val reply = queueTestProbe.receiveMessage()

    reply.state shouldBe initialState.togglePlay()
  }

  it should "toggle shuffle" in new Context {
    playingBehavior.run(ToggleShuffle(queueTestProbe.ref))

    val reply = queueTestProbe.receiveMessage()

    reply.state shouldBe initialState.toggleShuffle()
  }

  it should "skip back to first track" in new Context {
    playingBehavior.run(SkipBack(queueTestProbe.ref))

    val reply = queueTestProbe.receiveMessage()

    reply.state shouldBe initialState.skipBack()
    reply shouldBe a [SkippedBackToFirst]
  }

  it should "skip to last track" in new Context {
    playingBehavior.run(Skip(queueTestProbe.ref))

    val reply = queueTestProbe.receiveMessage()

    reply.state shouldBe initialState.skip()
    reply shouldBe a [SkippedToLastTrack]
  }

  it should "stop" in new Context {
    playingBehavior.run(Stop(queueTestProbe.ref))

    val reply = queueTestProbe.receiveMessage()

    reply.state shouldBe initialState
    reply shouldBe a [Stopped]
  }
}
