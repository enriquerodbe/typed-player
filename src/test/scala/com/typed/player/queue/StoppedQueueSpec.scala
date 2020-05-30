package com.typed.player.queue

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import com.typed.player.BaseSpec
import com.typed.player.models.{Queue, Track}
import com.typed.player.queue.QueueCommands.{EnqueueFirstTrack, ToggleShuffle}

class StoppedQueueSpec extends BaseSpec {

  trait Context {
    val testTrack = Track("test")
    val initialState = Queue.empty
    val stoppedBehavior = BehaviorTestKit(QueueBehaviorFactory.initial())
  }

  "a stopped queue" should "enqueue the first track" in new Context {
    val enqueue = EnqueueFirstTrack(testTrack, queueTestProbe.ref)

    stoppedBehavior.run(enqueue)

    val reply = queueTestProbe.receiveMessage()
    reply.state shouldBe initialState.enqueue(testTrack)
  }

  it should "toggle shuffle" in new Context {
    val toggleShuffle = ToggleShuffle(queueTestProbe.ref)

    stoppedBehavior.run(toggleShuffle)

    val reply = queueTestProbe.receiveMessage()
    reply.state shouldBe initialState.toggleShuffle()
  }
}
