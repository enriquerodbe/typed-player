package com.typed.player

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import com.typed.player.player_interface.PlayerInterfaceReplies
import com.typed.player.queue.QueueReplies
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait BaseSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  val testKit: ActorTestKit = ActorTestKit()
  val playerInterfaceProbe =
    testKit.createTestProbe[PlayerInterfaceReplies.Reply]("player-interface-probe")
  val queueTestProbe = testKit.createTestProbe[QueueReplies.Reply]("queue-test-probe")
  override protected def afterAll(): Unit = testKit.shutdownTestKit()
}
